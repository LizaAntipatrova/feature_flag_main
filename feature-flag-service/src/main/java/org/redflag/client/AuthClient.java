package org.redflag.client;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;
import org.redflag.dto.auth.NodeUuidsDTO;
import org.redflag.dto.auth.LoginForServiceCredentialsDto;
import org.redflag.dto.auth.CreateServiceCredentialsResponse;
import org.redflag.dto.auth.UserDTO;

@Client(id = "auth-service")
public interface AuthClient {
    @Get("/api/v1/clients/me")
    Publisher<UserDTO> getUser(@Header(HttpHeaders.COOKIE) String cookieHeader);


    @Post("/api/v1/sdk-clients/create")
    CreateServiceCredentialsResponse createServiceCredentials(@Header(HttpHeaders.COOKIE) String cookieHeader,
                                                              @Body LoginForServiceCredentialsDto request);

    @Delete("/api/v1/sdk-clients/delete")
    void deleteServiceCredentials(@Header(HttpHeaders.COOKIE) String cookieHeader,
                                  @Body LoginForServiceCredentialsDto request);

    @Delete("/api/v1/clients/delete-clients")
    void deleteClientsByNodeUuids(@Body NodeUuidsDTO request);

    @Delete("/api/v1/sdk-clients/delete-by-nodes")
    void deleteServiceCredentialsByNodeUuids(@Body NodeUuidsDTO request);

}
