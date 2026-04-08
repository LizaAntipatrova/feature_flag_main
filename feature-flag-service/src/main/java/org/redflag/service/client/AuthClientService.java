package org.redflag.service.client;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.client.AuthClient;
import org.redflag.dto.auth.CreateServiceCredentialsResponse;
import org.redflag.dto.auth.NodeUuidsDTO;
import org.redflag.dto.auth.LoginForServiceCredentialsDto;

@Singleton
@RequiredArgsConstructor
public class AuthClientService {
    private final AuthClient authClient;

    public CreateServiceCredentialsResponse createServiceCredentials(String sessionCookie, LoginForServiceCredentialsDto request){
        return authClient.createServiceCredentials(
                AuthenticationProvider.SESSION_COOKIE_NAME + "=" + sessionCookie,
                request);
    }

    public void deleteServiceCredentials(String sessionCookie, LoginForServiceCredentialsDto request){
        authClient.deleteServiceCredentials(
                AuthenticationProvider.SESSION_COOKIE_NAME + "=" + sessionCookie,
                request);
    }

    public void deleteClientsByNodeUuids(NodeUuidsDTO request){
        authClient.deleteClientsByNodeUuids(
                request);
    }

    public void deleteServiceCredentialsByNodeUuids(NodeUuidsDTO request){
        authClient.deleteServiceCredentialsByNodeUuids(
                request);
    }
}
