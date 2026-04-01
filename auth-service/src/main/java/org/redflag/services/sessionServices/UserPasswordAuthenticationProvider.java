package org.redflag.services.sessionServices;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.ReactiveAuthenticationProvider;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.redflag.constants.SecurityConstants;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class UserPasswordAuthenticationProvider implements
        ReactiveAuthenticationProvider<HttpRequest<?>, String, String> {

    private final UiClientAuthService uiClientAuthService;

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> request,
                                                          AuthenticationRequest<String, String> authRequest) {

        return uiClientAuthService.authenticate(authRequest.getIdentity(), authRequest.getSecret())
                .map(dto -> AuthenticationResponse.success(
                        dto.login(),
                        dto.roles(),
                        Map.of(SecurityConstants.UI_CLIENT_ID_NAME, dto.id(),
                                SecurityConstants.UI_DEPARTMENT_NAME, dto.uuidDepartament())
                ))
                .onErrorResume(e -> Mono.just(AuthenticationResponse.failure(e.getMessage())));
    }
}
