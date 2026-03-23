package org.redflag.service.impl.organization;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.dto.organization.create.CreateOrganizationRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationDTOMapper;

import java.util.Objects;

@RequiredArgsConstructor
@Singleton
public class CreateOrganizationService extends BaseService<CreateOrganizationRequest, OrganizationDTO> {
    private final OrganizationRepository organizationRepository;

    @Override
    protected void validateRequest(CreateOrganizationRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("name");
        }
    }

    @Override
    protected void validateState(CreateOrganizationRequest request) {
        if (organizationRepository.existsByName(request.getName())) {
            throw ErrorCatalog.NOT_UNIQUE_ORGANIZATION_NAME.getException();
        }
    }

    @Override
    protected OrganizationDTO execute(CreateOrganizationRequest request) {
        Organization organization = new Organization()
                .setName(request.getName());

        organizationRepository.save(organization);
        return OrganizationDTOMapper.toOrganizationDTO(organization);
    }
}
