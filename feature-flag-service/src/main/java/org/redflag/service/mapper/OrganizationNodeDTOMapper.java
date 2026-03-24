package org.redflag.service.mapper;

import jakarta.inject.Singleton;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.model.OrganizationNode;
@Singleton
public class OrganizationNodeDTOMapper {

    public OrganizationNodeDTO toOrganizationNodeDTO(OrganizationNode organizationNode) {
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
