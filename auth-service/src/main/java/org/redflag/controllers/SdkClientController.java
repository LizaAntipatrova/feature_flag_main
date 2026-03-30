package org.redflag.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.redflag.annotations.NoSdkAllowed;
import org.redflag.dto.CreateServiceAccessResponse;
import org.redflag.dto.SdkLoginRequest;
import org.redflag.services.SdkClientService;

@Controller("/api/v1/sdk-clients")
@RequiredArgsConstructor
@NoSdkAllowed
@Validated
@Tag(name = "CRUD методы для сущности Sdk клиент")
public class SdkClientController {

    private final SdkClientService sdkService;

    @Post("/create")
    @Status(HttpStatus.CREATED)
    public CreateServiceAccessResponse create(@Body @Valid SdkLoginRequest request) {
        return sdkService.createSdkWithKafka(request.newLogin());
    }

//    @Patch("/{id}/login")
//    public SdkClientResponse updateLogin(@PathVariable Long id, @Body @Valid SdkLoginRequest request) {
//        return sdkService.updateLogin(id, request.newLogin());
//    }
//
//    @Patch("/{id}/password")
//    public SdkClientResponse regeneratePassword(@PathVariable Long id) {
//        return sdkService.regeneratePassword(id);
//    }

    @Delete("/delete")
    public HttpResponse<?> delete(@Body @Valid SdkLoginRequest request) {
        sdkService.deleteWithKafka(request.newLogin());
        return HttpResponse.noContent();
    }
}