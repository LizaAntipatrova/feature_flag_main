package org.redflag.service.impl.organization;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.dto.organization.OrganizationIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationDTOMapper;

@RequiredArgsConstructor
@Singleton
public class GetOrganizationByIdService extends BaseService<OrganizationIdDTO, OrganizationDTO> {
    private final OrganizationRepository organizationRepository;

    @Override
    protected OrganizationDTO execute(OrganizationIdDTO request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return OrganizationDTOMapper.toOrganizationDTO(organization);
    }
}
