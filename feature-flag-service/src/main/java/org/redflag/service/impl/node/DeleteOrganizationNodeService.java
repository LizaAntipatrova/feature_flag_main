package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;

@Singleton
@RequiredArgsConstructor
public class DeleteOrganizationNodeService extends BaseService<OrganizationNodeIdDTO, Void> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateState(OrganizationNodeIdDTO request) {
        if (!organizationNodeRepository.existsChildNodeInParentNodeByChildIdAndParentUuid(
                request.getNodeId(),
                authenticationProvider.getAuthenticationNodeUuid()
        )){
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
        if (!organizationNodeRepository.isNodeInOrganization(request.getNodeId(), request.getOrganizationId())){
            throw ErrorCatalog.NO_SUCH_NODE_IN_ORGANIZATION.getException();
        }

    }

    @Override
    protected Void execute(OrganizationNodeIdDTO request) {
        organizationNodeRepository.deleteSubtree(request.getNodeId());
        return null;
    }
}
