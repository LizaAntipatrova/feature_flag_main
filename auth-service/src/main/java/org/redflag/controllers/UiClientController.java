package org.redflag.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.redflag.annotations.NoSdkAllowed;
import org.redflag.constants.UiClientRolesValue;
import org.redflag.dto.UiClientDto;
import org.redflag.dto.UpdateDepartmentUiClientRequest;
import org.redflag.dto.UpdateRolesUiClientRequest;
import org.redflag.dto.UpdateUiClientRequest;
import org.redflag.services.UiClientService;

import java.util.List;
import java.util.UUID;

@Controller("/api/v1/clients")
@RequiredArgsConstructor
@NoSdkAllowed
@Secured(SecurityRule.IS_AUTHENTICATED)
@Tag(name = "CRUD методы для сущности ui пользователь")
public class UiClientController {

    private final UiClientService clientService;

    @Get("/by-department/{uuidDepartament}")
    @Secured(UiClientRolesValue.UPDATE_EMPLOYEE)
    public List<UiClientDto> getClientsByDept(@QueryValue UUID uuidDepartament) {
        return clientService.getAllByDepartment(uuidDepartament);
    }

    @Get("/me")
    public UiClientDto getMe(Authentication authentication) {
        return clientService.getByLogin(authentication.getName());
    }

    @Delete("/{id}")
    public HttpResponse<?> delete(@PathVariable Long id) {
        clientService.delete(id);
        return HttpResponse.noContent();
    }

    @Patch("/me")
    public HttpResponse<?> updateMyCredentials(Authentication auth, @Body @Valid UpdateUiClientRequest request) {
        clientService.updateUiClient(auth.getName(), request);
        return HttpResponse.ok();
    }


    @Post("/{id}/roles/add")
    @Secured(UiClientRolesValue.UPDATE_EMPLOYEE)
    public HttpResponse<?> addRoles(@PathVariable Long id, @Body @Valid UpdateRolesUiClientRequest request) {
        clientService.addRoles(id, request.roleNames());
        return HttpResponse.ok();
    }

    @Delete("/{id}/roles/remove")
    @Secured(UiClientRolesValue.UPDATE_EMPLOYEE)
    public HttpResponse<?> removeRoles(@PathVariable Long id, @Body @Valid UpdateRolesUiClientRequest request) {
        clientService.removeRoles(id, request.roleNames());
        return HttpResponse.ok();
    }

    @Patch("/{id}/department")
    @Secured(UiClientRolesValue.UPDATE_EMPLOYEE)
    public HttpResponse<?> updateClientDepartment(@PathVariable Long id, @Body @Valid UpdateDepartmentUiClientRequest request) {
        clientService.updateDepartment(id, request.uuidDepartament());
        return HttpResponse.ok();
    }
}