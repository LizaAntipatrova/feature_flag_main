package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.organization.get.GetOrganizationByIdRequest;
import org.redflag.dto.organization.get.GetOrganizationByIdResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;

import java.util.Optional;

@RequiredArgsConstructor
@Singleton
public class GetOrganizationByIdService extends BaseService<GetOrganizationByIdRequest, GetOrganizationByIdResponse> {
    private final OrganizationRepository organizationRepository;

    @Override
    protected GetOrganizationByIdResponse execute(GetOrganizationByIdRequest request) {
        Optional<Organization> organizationOpt = organizationRepository.findById(request.organizationId());
        if (organizationOpt.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        Organization organization = organizationOpt.get();
        return new GetOrganizationByIdResponse(
                organization.getId(),
                organization.getName());
    }


}
