package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.organization.create.CreateOrganizationRequest;
import org.redflag.dto.organization.create.CreateOrganizationResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.AbstractService;

import java.util.Objects;

@RequiredArgsConstructor
@Singleton
public class CreateOrganizationService extends AbstractService<CreateOrganizationRequest, CreateOrganizationResponse> {
    private final OrganizationRepository organizationRepository;

    @Override
    protected void validateRequest(CreateOrganizationRequest createOrganizationRequest) {
        String name = createOrganizationRequest.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("name");
        }
    }

    @Override
    protected void validateState(CreateOrganizationRequest createOrganizationRequest) {
        if (organizationRepository.existsByName(createOrganizationRequest.getName())){
            throw ErrorCatalog.NOT_UNIQUE_ORGANIZATION_NAME.getException();
        }
    }

    @Override
    protected CreateOrganizationResponse logic(CreateOrganizationRequest createOrganizationRequest) {
        Organization organization = new Organization()
                .setName(createOrganizationRequest.getName());

        organizationRepository.save(organization);
        return new CreateOrganizationResponse(organization.getId(), organization.getName());
    }
}
