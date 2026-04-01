package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;

@RequiredArgsConstructor
@Singleton
public class GetOrganizationNodeByIdService extends BaseService<OrganizationNodeIdDTO, OrganizationNodeDTO> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateState(OrganizationNodeIdDTO request) {
        if (!organizationNodeRepository.isNodeInOrganization(
                authenticationProvider.getAuthenticationNodeUuid(),
                request.getOrganizationId())){
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
        if (!organizationNodeRepository.isNodeInOrganization(
                request.getNodeId(),
                request.getOrganizationId())){
            throw ErrorCatalog.NO_SUCH_NODE_IN_ORGANIZATION.getException();
        }
    }

    @Override
    protected OrganizationNodeDTO execute(OrganizationNodeIdDTO request) {
        OrganizationNode organizationNode = organizationNodeRepository
                .findByOrganization_IdAndId(request.getOrganizationId(), request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        return organizationNodeDTOMapper.toOrganizationNodeDTO(organizationNode);
    }
}
