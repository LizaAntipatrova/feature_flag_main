package org.redflag.services.tokenServices;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redflag.constants.SecurityConstants;
import org.redflag.exception.BadCredentialsCustomException;
import org.redflag.repositories.SdkClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class SdkAuthService {

    private final SdkClientRepository sdkClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator tokenGenerator;

    public Optional<String> authenticate(UsernamePasswordCredentials credentials) {
        UUID loginUuid = SupportTokenUtils.parseUuidOrThrow(credentials.getUsername());

        return sdkClientRepository.findByLogin(loginUuid)
                .filter(client -> passwordEncoder.matches(credentials.getPassword(), client.getPassword()))
                .flatMap(client -> {
                    Authentication authentication = Authentication.build(
                            client.getLogin().toString(),
                            Map.of(SecurityConstants.SDK_TOKEN_TYPE, SecurityConstants.SDK_TOKEN_TYPE_VALUE)
                    );

                    return tokenGenerator.generateToken(authentication, SecurityConstants.EXPIRATION_TOKEN_SECONDS);
                });
    }

}