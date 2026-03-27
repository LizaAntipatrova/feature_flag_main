package org.redflag.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redflag.constants.SecurityConstants;
import org.redflag.services.tokenServices.SdkAuthService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Controller("/api/v1/sdk")
@RequiredArgsConstructor
@Tag(name = "Авторизация для Sdk клиентов через jwt-токен")
public class SdkAuthController {

    private final SdkAuthService sdkAuthService;

    @Post("/login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Mono<HttpResponse<?>> login(@Body UsernamePasswordCredentials credentials) {
        return Mono.fromCallable(() -> {
                    var tokenOptional = sdkAuthService.authenticate(credentials);

                    if (tokenOptional.isPresent()) {
                        return HttpResponse.ok(Map.of(
                                SecurityConstants.SDK_ACCESS_TOKEN_NAME, tokenOptional.get(),
                                SecurityConstants.SDK_AUTH_TOKEN_TYPE, SecurityConstants.SDK_AUTH_TOKEN_VALUE
                        ));
                    } else {
                        return HttpResponse.unauthorized();
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(response -> response);
    }
}