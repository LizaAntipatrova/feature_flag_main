package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.dto.node.get.GetChildrenOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetChildrenOrganizationNodesService extends BaseService<OrganizationNodeIdDTO, GetChildrenOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;

    @Override
    protected void validateState(OrganizationNodeIdDTO request) {
        authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
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
