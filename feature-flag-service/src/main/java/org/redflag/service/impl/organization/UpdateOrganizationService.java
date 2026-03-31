package org.redflag.service.impl.organization;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.organization.update.UpdateOrganizationRequest;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationDTOMapper;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class UpdateOrganizationService extends BaseService<UpdateOrganizationRequest, OrganizationDTO> {
    private final OrganizationRepository organizationRepository;
    private final OrganizationDTOMapper organizationDTOMapper;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateRequest(UpdateOrganizationRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("name");
        }
    }

    @Override
    protected void validateState(UpdateOrganizationRequest request) {
        if (!organizationNodeRepository.isRootNodeInOrganization(
                authenticationProvider.getAuthenticationNodeUuid(),
                request.getId()
        )){
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
        Long id = request.getId();
        if (!organizationRepository.existsById(id)) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        String name = request.getName();
        if (organizationRepository.existsByName(name)) {
            throw ErrorCatalog.NOT_UNIQUE_ORGANIZATION_NAME.getException();
        }
    }

    @Override
    protected OrganizationDTO execute(UpdateOrganizationRequest request) {
        Organization organization = organizationRepository.findById(request.getId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        organization.setName(request.getName());
        Organization newOrganization = organizationRepository.update(organization);
        return organizationDTOMapper.toOrganizationDTO(newOrganization);
    }
}
