package org.redflag.service.impl.organization;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.organization.OrganizationIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;

@Singleton
@RequiredArgsConstructor
public class DeleteOrganizationService extends BaseService<OrganizationIdDTO, Void> {
    private final OrganizationRepository organizationRepository;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateState(OrganizationIdDTO request) {
        if (!organizationNodeRepository.isRootNodeInOrganization(
                authenticationProvider.getAuthenticationNodeUuid(),
                request.getOrganizationId()
        )){
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
    }

    @Override
    protected Void execute(OrganizationIdDTO request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        organizationRepository.delete(organization);
        return null;
    }
}
