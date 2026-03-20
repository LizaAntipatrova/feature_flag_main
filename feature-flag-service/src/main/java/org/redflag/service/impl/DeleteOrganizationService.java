package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.organization.delete.DeleteOrganizationRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;

import java.util.Optional;

@Singleton
@RequiredArgsConstructor
public class DeleteOrganizationService extends BaseService<DeleteOrganizationRequest, Void> {
    private final OrganizationRepository organizationRepository;

    @Override
    protected Void execute(DeleteOrganizationRequest request) {
        Optional<Organization> organizationOpt = organizationRepository.findById(request.organizationId());
        if (organizationOpt.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        Organization organization = organizationOpt.get();
        organizationRepository.delete(organization);
        return null;
    }
}
