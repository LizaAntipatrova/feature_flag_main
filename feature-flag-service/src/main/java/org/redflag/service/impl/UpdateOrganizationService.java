package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.organization.create.CreateOrganizationResponse;
import org.redflag.dto.organization.update.UpdateOrganizationRequest;
import org.redflag.dto.organization.update.UpdateOrganizationResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.AbstractService;

import java.util.Objects;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor
public class UpdateOrganizationService extends AbstractService<UpdateOrganizationRequest, UpdateOrganizationResponse> {
    private final OrganizationRepository organizationRepository;

    @Override
    protected void validateRequest(UpdateOrganizationRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.NOT_EMPTY.withMessageArgs("name");
        }
    }

    @Override
    protected void validateState(UpdateOrganizationRequest request) {
        Long id = request.getId();
        if(!organizationRepository.existsById(id)){
            throw ErrorCatalog.NO_DATA.getException();
        }
        String name = request.getName();
        if (organizationRepository.existsByName(name)){
            throw ErrorCatalog.NOT_UNIQUE_ORGANIZATION_NAME.getException();
        }
    }

    @Override
    protected UpdateOrganizationResponse logic(UpdateOrganizationRequest request) {
        Organization organization = new Organization().setId(request.getId()).setName(request.getName());
        Organization newOrganization = organizationRepository.update(organization);
        return new UpdateOrganizationResponse(newOrganization.getId(), newOrganization.getName());
    }
}
