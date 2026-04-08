package org.redflag.service.mapper;

import jakarta.inject.Singleton;
import org.redflag.dto.node.OrganizationNodeWithCredentialsDTO;
import org.redflag.model.OrganizationNode;

@Singleton
public class OrganizationNodeWithCredentialsDTOMapper {

    public OrganizationNodeWithCredentialsDTO toOrganizationNodeWithCredentialsDTO(OrganizationNode organizationNode) {
        return OrganizationNodeWithCredentialsDTO.builder()
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
