package org.redflag.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.RegisterOrganizationRequest;
import org.redflag.services.RegistrationService;

// доделать, переделать
@Controller("/api/v1/auth")
@Tag(name = "Регистрация ui пользователей, организаций и подчиненных")
@RequiredArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
public class RegistrationController {

    private final RegistrationService registrationService;

    @Post("/register")
    public HttpResponse<?> register(@Body @Valid RegisterOrganizationRequest request) {
        registrationService.registerOrganization(request);

        return HttpResponse.status(HttpStatus.CREATED);
    }
}
