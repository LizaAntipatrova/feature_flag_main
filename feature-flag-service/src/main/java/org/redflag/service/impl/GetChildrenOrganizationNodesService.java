package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.get.GetChildrenOrganizationNodesRequest;
import org.redflag.dto.node.get.GetChildrenOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetChildrenOrganizationNodesService extends BaseService<GetChildrenOrganizationNodesRequest, GetChildrenOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected GetChildrenOrganizationNodesResponse execute(GetChildrenOrganizationNodesRequest request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findAllChildrenById(request.organizationId(), request.nodeId());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return new GetChildrenOrganizationNodesResponse(request.nodeId(),
                organizationNodes.stream()
                        .map(this::toOrganizationNodeDTO)
                        .toList());
    }

    private GetChildrenOrganizationNodesResponse.OrganizationNodeDTO toOrganizationNodeDTO(OrganizationNode organizationNode) {
        return new GetChildrenOrganizationNodesResponse.OrganizationNodeDTO(organizationNode.getId(),
                organizationNode.getOrganization().getId(),
                organizationNode.getUuid(),
                organizationNode.getPath(),
                organizationNode.getName(),
                organizationNode.getIsService(),
                organizationNode.getVersion());
    }
}
