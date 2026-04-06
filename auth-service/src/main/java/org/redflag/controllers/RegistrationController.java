package org.redflag.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.EmployeeRegisterRequest;
import org.redflag.dto.RegisterOrganizationRequest;
import org.redflag.services.uiClientRegistrationServices.RegistrationEmployeeService;
import org.redflag.services.uiClientRegistrationServices.RegistrationOrganizationService;

@Controller("/api/v1/auth")
@Tag(name = "Регистрация ui пользователей, организаций и подчиненных")
@RequiredArgsConstructor
@Validated
@Secured(SecurityRule.IS_ANONYMOUS)
public class RegistrationController {

    private final RegistrationOrganizationService registrationOrganizationService;
    private final RegistrationEmployeeService registrationEmployeeService;

    @Post("/register-organization")
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<?> registerOrganization(@Body @Valid RegisterOrganizationRequest request) {
        registrationOrganizationService.registerOrganization(request);

        return HttpResponse.status(HttpStatus.CREATED);
    }

    @Post("/register-employee")
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<?> registerEmployee(
            @QueryValue @NotBlank String token,
            @Body @Valid EmployeeRegisterRequest request
    ) {
        registrationEmployeeService.registerEmployee(request, token);

        return HttpResponse.status(HttpStatus.CREATED);
    }

}
