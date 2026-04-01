package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.dto.node.get.GetAncestorsOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetAncestorsOrganizationNodesService extends BaseService<OrganizationNodeIdDTO, GetAncestorsOrganizationNodesResponse> {
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
    protected GetAncestorsOrganizationNodesResponse execute(OrganizationNodeIdDTO request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository
                .findAllAncestorsById(request.getOrganizationId(), request.getNodeId());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return toGetAncestorsOrganizationNodesResponse(request, organizationNodes);
    }

    private GetAncestorsOrganizationNodesResponse toGetAncestorsOrganizationNodesResponse(
            OrganizationNodeIdDTO request,
            List<OrganizationNode> organizationNodes
    ) {
        return GetAncestorsOrganizationNodesResponse.builder()
                .nodeId(request.getNodeId())
                .items(organizationNodes.stream()
                        .map(organizationNodeDTOMapper::toOrganizationNodeDTO)
                        .toList())
                .build();
    }


}
