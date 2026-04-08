package org.redflag.client;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;
import org.redflag.dto.auth.*;

@Client(id = "auth-service")
public interface AuthClient {
    @Get("/api/v1/clients/me")
    Publisher<UserDTO> getUser(@Header(HttpHeaders.COOKIE) String cookieHeader);


    @Post("/api/v1/sdk-clients/create")
    CreateServiceCredentialsResponse createServiceCredentials(@Header(HttpHeaders.AUTHORIZATION) String jwt,
                                                              @Body LoginForServiceCredentialsDto request);

    @Delete("/api/v1/sdk-clients/delete")
    void deleteServiceCredentials(@Header(HttpHeaders.AUTHORIZATION) String jwt,
                                  @Body LoginForServiceCredentialsDto request);

    @Delete("/api/v1/clients/delete-clients")
    void deleteClientsByNodeUuids(@Header(HttpHeaders.AUTHORIZATION) String jwt,
                                  @Body NodeUuidsDTO request);

    @Delete("/api/v1/sdk-clients/delete-sdks")
    void deleteServiceCredentialsByNodeUuids(@Header(HttpHeaders.AUTHORIZATION) String jwt,
                                             @Body SdkUuidsDTO request);

    @Post("/api/v1/internal/token/main-service")
    JwtDTO getMainServiceJwt();
}
