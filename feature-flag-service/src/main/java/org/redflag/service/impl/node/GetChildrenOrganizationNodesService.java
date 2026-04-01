package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.dto.node.get.GetChildrenOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetChildrenOrganizationNodesService extends BaseService<OrganizationNodeIdDTO, GetChildrenOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateState(OrganizationNodeIdDTO request) {
        if (!organizationNodeRepository.isNodeInOrganization(
                authenticationProvider.getAuthenticationNodeUuid(),
                request.getOrganizationId())) {
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
        if (!organizationNodeRepository.isNodeInOrganization(
                request.getNodeId(),
                request.getOrganizationId())) {
            throw ErrorCatalog.NO_SUCH_NODE_IN_ORGANIZATION.getException();
        }
    }

    @Override
    protected GetChildrenOrganizationNodesResponse execute(OrganizationNodeIdDTO request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository
                .findAllChildrenById(request.getOrganizationId(), request.getNodeId());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return toGetChildrenOrganizationNodesResponse(request, organizationNodes);
    }

    private GetChildrenOrganizationNodesResponse toGetChildrenOrganizationNodesResponse(OrganizationNodeIdDTO request, List<OrganizationNode> organizationNodes) {
        return GetChildrenOrganizationNodesResponse.builder()
                .nodeId(request.getNodeId())
                .items(organizationNodes.stream()
                        .map(organizationNodeDTOMapper::toOrganizationNodeDTO)
                        .toList())
                .build();
    }


}
