package org.redflag.service.validator;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.error.ErrorCatalog;
import org.redflag.repository.OrganizationNodeRepository;

@Singleton
@RequiredArgsConstructor
public class AuthRightsToNodeValidator {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    public void checkIsAuthNodeIsParentToRequestNode(Long requestNodeId) {
        if (!organizationNodeRepository.existsChildNodeInParentNodeByChildIdAndParentUuid(
                requestNodeId,
                authenticationProvider.getAuthenticationNodeUuid()
        )) {
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
    }

    public void checkIsAuthNodeInOrganization(Long organizationId) {
        if (!organizationNodeRepository.isNodeInOrganization(
                authenticationProvider.getAuthenticationNodeUuid(),
                organizationId)) {
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
    }

    public void checkAuthNodeIsRootNode(Long organizationId) {
        if (!organizationNodeRepository.isRootNodeInOrganization(
                authenticationProvider.getAuthenticationNodeUuid(),
                organizationId
        )) {
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
    }
}
