package org.redflag.service.mapper;

import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.model.Organization;

public class OrganizationDTOMapper {
    public static OrganizationDTO toOrganizationDTO(Organization organization) {
        return OrganizationDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .build();
    }
}
