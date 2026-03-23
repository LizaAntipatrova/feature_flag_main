package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.OrganizationNodeDTO;
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
                .findAllDescendantsByIdAndDepth(request.getOrganizationId(), request.getNodeId(), request.getDepth());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return new GetDescendantsOrganizationNodesResponse(request.getNodeId(),
                request.getDepth(),
                organizationNodes.stream()
                        .map(this::toOrganizationNodeDTO)
                        .toList());
    }

    private OrganizationNodeDTO toOrganizationNodeDTO(OrganizationNode organizationNode) {
        return OrganizationNodeDTO.builder()
                .id(organizationNode.getId())
                .organizationId(organizationNode.getOrganization().getId())
                .uuid(organizationNode.getUuid())
                .path(organizationNode.getPath())
                .name(organizationNode.getName())
                .isService(organizationNode.getIsService())
                .version(organizationNode.getVersion())
                .build();
    }
}
