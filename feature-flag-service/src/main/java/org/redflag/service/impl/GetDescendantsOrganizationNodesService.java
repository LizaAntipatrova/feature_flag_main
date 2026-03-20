package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.get.GetDescendantsOrganizationNodesRequest;
import org.redflag.dto.node.get.GetDescendantsOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetDescendantsOrganizationNodesService extends BaseService<GetDescendantsOrganizationNodesRequest, GetDescendantsOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected GetDescendantsOrganizationNodesResponse execute(GetDescendantsOrganizationNodesRequest request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository
                .findAllDescendantsByIdAndDepth(request.organizationId(), request.nodeId(), request.depth());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return new GetDescendantsOrganizationNodesResponse(request.nodeId(),
                request.depth(),
                organizationNodes.stream()
                        .map(this::toOrganizationNodeDTO)
                        .toList());
    }

    private GetDescendantsOrganizationNodesResponse.OrganizationNodeDTO toOrganizationNodeDTO(OrganizationNode organizationNode) {
        return new GetDescendantsOrganizationNodesResponse.OrganizationNodeDTO(organizationNode.getId(),
                organizationNode.getOrganization().getId(),
                organizationNode.getUuid(),
                organizationNode.getPath(),
                organizationNode.getName(),
                organizationNode.getIsService(),
                organizationNode.getVersion());
    }
}
