package org.redflag.controllers;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.redflag.constants.UiClientRolesValue;
import org.redflag.dto.GenerateInviteRequest;
import org.redflag.dto.GenerateInviteResponse;
import org.redflag.services.uiClientRegistrationServices.InviteService;

@Controller("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UiClientInviteController {

    private final InviteService inviteService;

    @Post("/generate-invite")
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Secured(UiClientRolesValue.CREATE_EMPLOYEE)
    public GenerateInviteResponse createInvite(Authentication authentication,
                                               @Body @Valid GenerateInviteRequest request) {
        return inviteService.generateInvite(authentication, request);
    }

}
