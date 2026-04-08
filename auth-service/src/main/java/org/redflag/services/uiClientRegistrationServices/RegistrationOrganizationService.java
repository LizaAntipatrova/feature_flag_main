package org.redflag.services.uiClientRegistrationServices;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import io.micronaut.transaction.annotation.Transactional;
import org.redflag.dto.CreateOrganizationResponse;
import org.redflag.dto.RegisterOrganizationRequest;
import org.redflag.entities.Role;
import org.redflag.entities.UiClient;
import org.redflag.exception.ConflictCustomException;
import org.redflag.repositories.RoleRepository;
import org.redflag.repositories.UiClientRepository;
import org.redflag.services.externalServices.FeatureFlagServiceClient;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class RegistrationOrganizationService {

    private final FeatureFlagServiceClient ffServiceClient;
    private final RegisterUiClient registerUiClient;

    public void registerOrganization(RegisterOrganizationRequest request) {

        CreateOrganizationResponse organizationResponse = ffServiceClient.createOrganization
                                                                    (request.organization_name());
        UUID departmentUuid = organizationResponse.organizationNode().uuid();

        registerUiClient.createUiUser(request, departmentUuid);
    }


}