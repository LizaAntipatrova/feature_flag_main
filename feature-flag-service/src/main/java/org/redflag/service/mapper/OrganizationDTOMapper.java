package org.redflag.service.mapper;

import jakarta.inject.Singleton;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.model.Organization;
@Singleton
public class OrganizationDTOMapper {
    public OrganizationDTO toOrganizationDTO(Organization organization) {
        return OrganizationDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .build();
    }
}
