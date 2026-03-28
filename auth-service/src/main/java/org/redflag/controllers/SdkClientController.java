package org.redflag.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.redflag.annotations.NoSdkAllowed;
import org.redflag.dto.SdkClientResponse;
import org.redflag.dto.SdkLoginRequest;
import org.redflag.services.SdkClientService;

@Controller("/api/v1/sdk-clients")
@RequiredArgsConstructor
//@Secured(SecurityRule.IS_AUTHENTICATED)
@NoSdkAllowed
@Tag(name = "CRUD методы для сущности Sdk клиент")
public class SdkClientController {

    private final SdkClientService sdkService;

    @Post
    @Status(HttpStatus.CREATED)
    public SdkClientResponse create(@Body @Valid SdkLoginRequest request) {
        return sdkService.create(request.newLogin());
    }

    @Patch("/{id}/login")
    public SdkClientResponse updateLogin(@PathVariable Long id, @Body @Valid SdkLoginRequest request) {
        return sdkService.updateLogin(id, request.newLogin());
    }

    @Patch("/{id}/password")
    public SdkClientResponse regeneratePassword(@PathVariable Long id) {
        return sdkService.regeneratePassword(id);
    }

    @Delete("/{id}")
    public HttpResponse<?> delete(@PathVariable Long id) {
        sdkService.delete(id);
        return HttpResponse.noContent();
    }
}