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
import org.redflag.service.validator.AuthRightsToNodeValidator;

@RequiredArgsConstructor
@Singleton
public class GetOrganizationByIdService extends BaseService<OrganizationIdDTO, OrganizationDTO> {
    private final OrganizationRepository organizationRepository;
    private final OrganizationDTOMapper organizationDTOMapper;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;

    @Override
    protected void validateState(OrganizationIdDTO request) {
        authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
    }

    @Override
    protected OrganizationDTO execute(OrganizationIdDTO request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return organizationDTOMapper.toOrganizationDTO(organization);
    }
}
