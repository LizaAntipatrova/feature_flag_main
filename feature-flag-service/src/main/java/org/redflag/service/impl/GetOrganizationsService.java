package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.organization.get.GetOrganizationsRequest;
import org.redflag.dto.organization.get.GetOrganizationsResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;
import org.redflag.validator.PaginationParameterValidator;

import java.util.List;

@RequiredArgsConstructor
@Singleton
public class GetOrganizationsService extends BaseService<GetOrganizationsRequest, GetOrganizationsResponse> {
    private final OrganizationRepository organizationRepository;

    @Override
    protected void validateRequest(GetOrganizationsRequest request) {
        if (!PaginationParameterValidator.validateLimit(request.limit())) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }
        if (!PaginationParameterValidator.validateOffset(request.offset())) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }
    }

    @Override
    protected GetOrganizationsResponse execute(GetOrganizationsRequest getOrganizationsRequest) {
        List<Organization> organizations = organizationRepository
                .findAll(getOrganizationsRequest.limit(), getOrganizationsRequest.offset());

        List<GetOrganizationsResponse.OrganizationDTO> organizationDTOS = organizations.stream()
                .map(this::toOrganizationDTO).toList();

        if (organizationDTOS.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }

        return new GetOrganizationsResponse(organizationDTOS,
                getOrganizationsRequest.limit(),
                getOrganizationsRequest.offset(),
                organizations.size());
    }

    private GetOrganizationsResponse.OrganizationDTO toOrganizationDTO(Organization organization) {
        return new GetOrganizationsResponse.OrganizationDTO(organization.getId(),
                organization.getName());
    }
}
