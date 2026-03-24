package org.redflag.service.impl.organization;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.PaginationDTO;
import org.redflag.dto.organization.get.GetOrganizationsResponse;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationDTOMapper;
import org.redflag.validator.PaginationParameterValidator;

import java.util.List;

@RequiredArgsConstructor
@Singleton
public class GetOrganizationsService extends BaseService<PaginationDTO, GetOrganizationsResponse> {
    private final OrganizationRepository organizationRepository;
    private final OrganizationDTOMapper organizationDTOMapper;

    @Override
    protected void validateRequest(PaginationDTO request) {
        if (!PaginationParameterValidator.validateLimit(request.getLimit())) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }
        if (!PaginationParameterValidator.validateOffset(request.getOffset())) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }
    }

    @Override
    protected GetOrganizationsResponse execute(PaginationDTO getOrganizationsRequest) {
        List<Organization> organizations = organizationRepository
                .findAll(getOrganizationsRequest.getLimit(), getOrganizationsRequest.getOffset());

        List<OrganizationDTO> organizationDTOS = organizations.stream()
                .map(organizationDTOMapper::toOrganizationDTO).toList();

        if (organizationDTOS.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }

        return toGetOrganizationsResponse(getOrganizationsRequest, organizationDTOS);
    }

    private static GetOrganizationsResponse toGetOrganizationsResponse(PaginationDTO getOrganizationsRequest, List<OrganizationDTO> organizationDTOS) {
        return GetOrganizationsResponse.builder()
                .items(organizationDTOS)
                .limit(getOrganizationsRequest.getLimit())
                .offset(getOrganizationsRequest.getOffset())
                .total(organizationDTOS.size())
                .build();
    }

}
