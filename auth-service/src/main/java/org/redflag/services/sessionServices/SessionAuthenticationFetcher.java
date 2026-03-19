package org.redflag.services.sessionServices;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.redflag.entities.Session;
import reactor.core.publisher.Mono;

import java.util.Map;

@Singleton
@RequiredArgsConstructor
public class SessionAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {

    private final SessionService sessionService;

    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        return Mono.justOrEmpty(extractId(request))
                .flatMap(sessionService::findActiveSession)
                .map(this::mapToAuth);
    }

    private Long extractId(HttpRequest<?> request) {
        return request.getCookies().get("SESSION", String.class).map(Long::valueOf).orElse(null);
    }

    private Authentication mapToAuth(Session session) {
        return Authentication.build(session.getUser().getLogin(), Map.of("id", session.getUser().getId()));
    }
}