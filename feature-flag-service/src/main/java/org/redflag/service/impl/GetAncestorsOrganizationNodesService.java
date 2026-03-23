package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.dto.node.get.GetAncestorsOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetAncestorsOrganizationNodesService extends BaseService<OrganizationNodeIdDTO, GetAncestorsOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;


    @Override
    protected GetAncestorsOrganizationNodesResponse execute(OrganizationNodeIdDTO request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findAllAncestorsById(request.getOrganizationId(), request.getNodeId());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return new GetAncestorsOrganizationNodesResponse(request.getNodeId(),
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
