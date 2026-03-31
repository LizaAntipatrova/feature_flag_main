package org.redflag.client;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;
import org.redflag.dto.auth.UserDTO;

import java.util.concurrent.Flow;

@Client(id = "auth-service")
public interface AuthClient {
    @Get("/api/v1/clients/me")
    Publisher<UserDTO> getUser(@Header(HttpHeaders.COOKIE) String cookieHeader);
}
