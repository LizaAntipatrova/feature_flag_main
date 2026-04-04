package org.redflag.service.impl.organization;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.dto.organization.update.UpdateOrganizationRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationDTOMapper;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.OrganizationNodeValidator;
import org.redflag.service.validator.UniqueNameValidator;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class UpdateOrganizationService extends BaseService<UpdateOrganizationRequest, OrganizationDTO> {
    private final OrganizationRepository organizationRepository;
    private final OrganizationDTOMapper organizationDTOMapper;
    private final OrganizationNodeValidator organizationNodeValidator;
    private final UniqueNameValidator uniqueNameValidator;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;

    @Override
    protected void validateRequest(UpdateOrganizationRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("name");
        }
    }

    @Override
    protected void validateState(UpdateOrganizationRequest request) {
        authRightsToNodeValidator.checkAuthNodeIsRootNode(request.getId());
        Organization organization = organizationRepository.findById(request.getId()).orElseThrow();
        if (!request.getName().equals(organization.getName())) {
            uniqueNameValidator.checkIsUniqueOrganizationName(request.getName());
        }

    }

    @Override
    @Transactional
    protected OrganizationDTO execute(UpdateOrganizationRequest request) {
        Organization organization = organizationRepository.findById(request.getId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        organization.setName(request.getName());
        Organization newOrganization = organizationRepository.update(organization);
        return organizationDTOMapper.toOrganizationDTO(newOrganization);
    }
}
