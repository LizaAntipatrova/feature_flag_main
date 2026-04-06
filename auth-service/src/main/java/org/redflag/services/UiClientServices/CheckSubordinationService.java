package org.redflag.services.UiClientServices;

import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.OrganizationNodeDTO;
import org.redflag.exception.ResourceNotFoundCustomException;
import org.redflag.repositories.UiClientRepository;
import org.redflag.services.externalServices.FeatureFlagServiceClient;

import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class CheckSubordinationService {

    private final FeatureFlagServiceClient ffServiceClient;
    private final UiClientRepository uiClientRepository;

    public boolean isSubordinatedFromAuth(Authentication authentication, UUID subordinateUserDepartmentUuid) {

        UUID authorizedUserDepartmentUuid = uiClientRepository.findUuidDepartamentByLogin(authentication.getName())
                .orElseThrow(() -> new  ResourceNotFoundCustomException("User not found"));

        return isSubordinated(authorizedUserDepartmentUuid, subordinateUserDepartmentUuid);
    }

    public boolean isSubordinated(UUID initializingUserDepartmentUuid, UUID subordinateUserDepartmentUuid) {
        OrganizationNodeDTO initializingUserNode = ffServiceClient.getOrganizationNodeByUuid(
                initializingUserDepartmentUuid
        );
        OrganizationNodeDTO subordinateUserNode = ffServiceClient.getOrganizationNodeByUuid(
                subordinateUserDepartmentUuid
        );
        return subordinateUserNode.path().startsWith(initializingUserNode.path()) &&
                initializingUserNode.organizationId().equals(subordinateUserNode.organizationId());
    }
}
