package org.redflag.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.Authenticator;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import org.redflag.exception.BadCredentialsCustomException;
import org.redflag.services.sessionServices.SessionService;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller("/api/v1/auth")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
public class LoginController {

    private final SessionService sessionService;
    private final Authenticator<HttpRequest<?>> authenticator;

    @Post("/login")
    public Mono<HttpResponse<?>> login(@Body UsernamePasswordCredentials credentials, HttpRequest<?> request) {
        return Mono.from(authenticator.authenticate(request, credentials))
                .filter(AuthenticationResponse::isAuthenticated)
                .switchIfEmpty(Mono.error(new BadCredentialsCustomException("Invalid login or password")))
                .flatMap(authResponse -> sessionService.createSession(credentials.getUsername()))
                .map(sessionService::buildSuccessResponse);
    }

    @Post("/logout")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<?> logout(HttpRequest<?> request) {
        request.getCookies().get("SESSION", String.class)
                .ifPresent(sessionService::invalidateSession);

        return HttpResponse.ok()
                .cookie(sessionService.createSessionCookie("", 0));
    }

    @Post("/logout-all")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<?> logoutAll(Authentication authentication) {
        Long userId = (Long) authentication.getAttributes().get("id");
        sessionService.invalidateAllUserSessions(userId);

        return HttpResponse.ok()
                .cookie(sessionService.createSessionCookie("", 0));
    }

    @Get("/introspect")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Map<String, Object> introspect(Authentication authentication) {
        return Map.of(
                "active", true,
                "userId", authentication.getAttributes().get("id"),
                "username", authentication.getName()
        );
    }

}