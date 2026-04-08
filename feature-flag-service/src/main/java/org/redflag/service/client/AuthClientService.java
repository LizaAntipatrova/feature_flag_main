package org.redflag.service.client;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.client.AuthClient;
import org.redflag.dto.auth.*;

@Singleton
@RequiredArgsConstructor
public class AuthClientService {
    private final AuthClient authClient;

    public CreateServiceCredentialsResponse createServiceCredentials(LoginForServiceCredentialsDto request) {
        JwtDTO jwtDTO = authClient.getMainServiceJwt();
        return authClient.createServiceCredentials(
                jwtDTO.getTokenType() + jwtDTO.getAccessToken(),
                request);
    }

    public void deleteServiceCredentials(LoginForServiceCredentialsDto request) {
        JwtDTO jwtDTO = authClient.getMainServiceJwt();
        authClient.deleteServiceCredentials(
                jwtDTO.getTokenType() + jwtDTO.getAccessToken(),
                request);
    }

    public void deleteClientsByNodeUuids(NodeUuidsDTO request) {
        JwtDTO jwtDTO = authClient.getMainServiceJwt();
        authClient.deleteClientsByNodeUuids(
                jwtDTO.getTokenType() + jwtDTO.getAccessToken(),
                request);
    }

    public void deleteServiceCredentialsByNodeUuids(SdkUuidsDTO request) {
        JwtDTO jwtDTO = authClient.getMainServiceJwt();
        authClient.deleteServiceCredentialsByNodeUuids(
                jwtDTO.getTokenType() + jwtDTO.getAccessToken(),
                request);
    }

}
