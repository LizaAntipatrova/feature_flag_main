package org.redflag.service.impl;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.organization.delete.DeleteOrganizationRequest;
import org.redflag.dto.organization.get.GetOrganizationByIdResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.AbstractService;

import java.util.Optional;

@Singleton
@RequiredArgsConstructor
public class DeleteOrganizationService extends AbstractService<DeleteOrganizationRequest, Void> {
    private final OrganizationRepository organizationRepository;

    @Override
    protected Void logic(DeleteOrganizationRequest request) {
        Optional<Organization> organizationOpt = organizationRepository.findById(request.organizationId());
        if (organizationOpt.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        Organization organization = organizationOpt.get();
        organizationRepository.delete(organization);
        return null;
    }
}
