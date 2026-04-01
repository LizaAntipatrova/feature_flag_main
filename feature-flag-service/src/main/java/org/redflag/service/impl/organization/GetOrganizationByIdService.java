package org.redflag.service.impl.organization;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.dto.organization.OrganizationIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationDTOMapper;

@RequiredArgsConstructor
@Singleton
public class GetOrganizationByIdService extends BaseService<OrganizationIdDTO, OrganizationDTO> {
    private final OrganizationRepository organizationRepository;
    private final OrganizationDTOMapper organizationDTOMapper;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateState(OrganizationIdDTO request) {
        if (!organizationNodeRepository.isNodeInOrganization(
                authenticationProvider.getAuthenticationNodeUuid(),
                request.getOrganizationId())){
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
    }

    @Override
    protected OrganizationDTO execute(OrganizationIdDTO request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return organizationDTOMapper.toOrganizationDTO(organization);
    }
}
