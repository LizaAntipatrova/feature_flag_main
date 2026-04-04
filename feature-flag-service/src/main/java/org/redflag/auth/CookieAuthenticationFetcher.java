package org.redflag.auth;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.redflag.client.AuthClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;


@Singleton
@RequiredArgsConstructor
public class CookieAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {
    private final AuthClient authClient;

    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        String cookieHeader = request.getHeaders().get(HttpHeaders.COOKIE);

        if (Objects.isNull(cookieHeader) || cookieHeader.isBlank()){
            return Mono.empty();
        }

        return Mono.from(authClient.getUser(cookieHeader))
                .map(userDTO -> Authentication.build(
                        userDTO.getLogin(),
                        Objects.isNull(userDTO.getRoles()) ? Set.of() : userDTO.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet()),
                        Map.of(AuthenticationProvider.NODE_UUID_ATTRIBUTE_NAME, userDTO.getUuidDepartament())
                )).onErrorResume(HttpClientResponseException.class,
                        e -> {
                    if (e.getStatus().equals(HttpStatus.UNAUTHORIZED)){
                        return Mono.empty();
                    }
                    return Mono.error(e);
                        }
                );
    }
}
