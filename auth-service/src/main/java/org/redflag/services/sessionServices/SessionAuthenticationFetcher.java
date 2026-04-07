package org.redflag.services.sessionServices;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.redflag.constants.SecurityConstants;
import org.redflag.entities.Session;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Singleton
@RequiredArgsConstructor
public class SessionAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {

    private final SessionService sessionService;

    private static final String TOKEN_API_PREFIX = "/api/v1/sdk-clients";
    private static final String TOKEN_DELETE_CLIENTS_API_PREFIX = "/api/v1/clients/delete-clients";


    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        String path = request.getPath();

        if (path.startsWith(TOKEN_API_PREFIX) ||
                path.startsWith(TOKEN_DELETE_CLIENTS_API_PREFIX)) {
            return Mono.empty();
        }

        return Mono.justOrEmpty(extractId(request))
                .flatMap(sessionService::findActiveSession)
                .map(this::mapToAuth);
    }

    private Long extractId(HttpRequest<?> request) {
        return request.getCookies().get(SecurityConstants.COOKIES_NAME, String.class).map(Long::valueOf).orElse(null);
    }

    private Authentication mapToAuth(Session session) {
        List<String> roles = session.getUser()
                                    .getRoles()
                                    .stream()
                .map(org.redflag.entities.Role::getName)
                .toList();

        return Authentication.build(
                session.getUser()
                        .getLogin(),
                roles,
                Map.of(SecurityConstants.UI_CLIENT_ID_NAME,
                        session.getUser().getId())
        );
    }
}