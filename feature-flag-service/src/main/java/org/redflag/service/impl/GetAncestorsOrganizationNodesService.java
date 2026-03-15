package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.get.GetAncestorsOrganizationNodesRequest;
import org.redflag.dto.node.get.GetAncestorsOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.AbstractService;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetAncestorsOrganizationNodesService extends AbstractService<GetAncestorsOrganizationNodesRequest, GetAncestorsOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;


    @Override
    protected GetAncestorsOrganizationNodesResponse logic(GetAncestorsOrganizationNodesRequest request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findAllAncestors(request.organizationId(),  request.nodeId());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return new GetAncestorsOrganizationNodesResponse(request.nodeId(),
                organizationNodes.stream()
                        .map(this::toOrganizationNodeDTO)
                        .toList());
    }
    private GetAncestorsOrganizationNodesResponse.OrganizationNodeDTO toOrganizationNodeDTO(OrganizationNode organizationNode) {
        return new GetAncestorsOrganizationNodesResponse.OrganizationNodeDTO(organizationNode.getId(),
                organizationNode.getOrganization().getId(),
                organizationNode.getUuid(),
                organizationNode.getPath(),
                organizationNode.getName(),
                organizationNode.getIsService(),
                organizationNode.getVersion());
    }
}
