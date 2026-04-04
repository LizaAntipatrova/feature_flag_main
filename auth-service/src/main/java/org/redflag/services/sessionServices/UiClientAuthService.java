package org.redflag.services.sessionServices;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.UserSecurityDto;
import org.redflag.entities.UiClient;
import org.redflag.exception.BadCredentialsCustomException;
import org.redflag.repositories.UiClientRepository;
import org.redflag.services.mappers.UiClientToUserSecurityDtoMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Singleton
@RequiredArgsConstructor
public class UiClientAuthService {
    private final UiClientRepository uiClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final UiClientToUserSecurityDtoMapper mapper;

    public Mono<UserSecurityDto> authenticate(String login, String password) {
        return findUser(login)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .switchIfEmpty(Mono.error(new BadCredentialsCustomException("Invalid login or password")))
                .map(mapper::mapToDto);
    }

    private Mono<UiClient> findUser(String login) {
        return Mono.fromCallable(() -> uiClientRepository.findByLogin(login))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty);
    }

}
