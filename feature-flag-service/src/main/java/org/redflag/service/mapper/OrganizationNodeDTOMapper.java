package org.redflag.service.mapper;

import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.model.OrganizationNode;

public class OrganizationNodeDTOMapper {

    public static OrganizationNodeDTO toOrganizationNodeDTO(OrganizationNode organizationNode) {
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
