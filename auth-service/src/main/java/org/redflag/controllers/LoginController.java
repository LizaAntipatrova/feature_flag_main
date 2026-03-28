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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redflag.constants.SecurityConstants;
import org.redflag.exception.BadCredentialsCustomException;
import org.redflag.services.sessionServices.SessionService;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller("/api/v1/auth")
//@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
@Tag(name = "Авторизация ui пользователей через сессии")
public class LoginController {

//    private static final String COOKIES_NAME = "SESSION";

    private final SessionService sessionService;
    private final Authenticator<HttpRequest<?>> authenticator;

    @Post("/login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Mono<HttpResponse<?>> login(@Body UsernamePasswordCredentials credentials, HttpRequest<?> request) {
        return Mono.from(authenticator.authenticate(request, credentials))
                .filter(AuthenticationResponse::isAuthenticated)
                .switchIfEmpty(Mono.error(new BadCredentialsCustomException("Invalid login or password")))
                .flatMap(authResponse -> sessionService.createSession(credentials.getUsername()))
                .map(sessionService::buildSuccessResponse);
    }

    @Post("/logout")
//    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<?> logout(HttpRequest<?> request, Authentication authentication) {
        String userLogin = authentication.getName();

        request.getCookies().get(SecurityConstants.COOKIES_NAME, String.class)
                .ifPresent(sessionId -> sessionService.invalidateSession(sessionId, userLogin));

        return HttpResponse.ok()
                .cookie(sessionService.createSessionCookie(StringUtils.EMPTY, 0));
    }

    @Post("/logout-all")
//    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<?> logoutAll(Authentication authentication) {
        Long userId = (Long) authentication.getAttributes().get("id");
        sessionService.invalidateAllUserSessions(userId);

        return HttpResponse.ok()
                .cookie(sessionService.createSessionCookie(StringUtils.EMPTY, 0));
    }

    @Get("/introspect")
//    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Map<String, Object> introspect(Authentication authentication) {
        return Map.of(
                "active", true,
                "userId", authentication.getAttributes().get("id"),
                "username", authentication.getName()
        );
    }

}