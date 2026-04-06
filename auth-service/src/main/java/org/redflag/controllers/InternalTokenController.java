package org.redflag.controllers;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import org.redflag.constants.SecurityConstants;
import org.redflag.services.tokenServices.InternalTokenService;
import java.util.Map;

@Controller("/api/v1/internal")
@RequiredArgsConstructor
public class InternalTokenController {

    private final InternalTokenService internalTokenService;

    @Post("/token/main-service")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> mainServiceToken() {

        String jwt = internalTokenService.generateInternalToken(
                SecurityConstants.MAIN_SERVICE_TOKEN_TYPE_VALUE
//                SecurityConstants.MAIN_SERVICE_TOKEN_TYPE_VALUE
        );

        return HttpResponse.ok(Map.of(
                SecurityConstants.SDK_ACCESS_TOKEN_NAME, jwt,
                SecurityConstants.SDK_AUTH_TOKEN_TYPE, SecurityConstants.SDK_AUTH_TOKEN_VALUE
        ));
    }
}
