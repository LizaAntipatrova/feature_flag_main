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
import org.redflag.service.validator.UniqueNameValidator;

import java.util.Objects;

@RequiredArgsConstructor
@Singleton
public class CreateOrganizationService extends BaseService<CreateOrganizationRequest, OrganizationDTO> {
    private final OrganizationRepository organizationRepository;
    private final OrganizationDTOMapper organizationDTOMapper;
    private final UniqueNameValidator uniqueNameValidator;

    @Override
    protected void validateRequest(CreateOrganizationRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("name");
        }
    }

    @Override
    protected void validateState(CreateOrganizationRequest request) {
        uniqueNameValidator.checkIsUniqueOrganizationName(request.getName());
    }

    @Override
    protected OrganizationDTO execute(CreateOrganizationRequest request) {
        Organization organization = new Organization()
                .setName(request.getName());

        organizationRepository.save(organization);
        return organizationDTOMapper.toOrganizationDTO(organization);
    }
}
