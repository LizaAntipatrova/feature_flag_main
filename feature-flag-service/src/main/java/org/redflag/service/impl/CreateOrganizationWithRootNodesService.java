package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.complex.CreateOrganizationWithRootNodeResponse;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.dto.organization.create.CreateOrganizationRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;
import org.redflag.service.impl.organization.CreateOrganizationService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;

import java.util.UUID;

@RequiredArgsConstructor
@Singleton
public class CreateOrganizationWithRootNodesService extends BaseService<CreateOrganizationRequest, CreateOrganizationWithRootNodeResponse> {
    private final CreateOrganizationService createOrganizationService;
    private final OrganizationRepository organizationRepository;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;

    @Override
    protected CreateOrganizationWithRootNodeResponse execute(CreateOrganizationRequest request) {
        OrganizationDTO organizationDTO = createOrganizationService
                .service(request);

        Organization organization = organizationRepository.findById(organizationDTO.getId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        OrganizationNode organizationNode = new OrganizationNode()
                .setName(organization.getName())
                .setUuid(UUID.randomUUID())
                .setOrganization(organization)
                .setIsService(false);
        organizationNodeRepository.save(organizationNode);
        organizationNode.setPath(organizationNode.getId().toString());
        organizationNodeRepository.update(organizationNode);


        return toCreateOrganizationWithRootNodeResponse(organization, organizationNode);
    }

    private CreateOrganizationWithRootNodeResponse toCreateOrganizationWithRootNodeResponse(
            Organization organization,
            OrganizationNode newOrganizationNode
    ) {
        return CreateOrganizationWithRootNodeResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .organizationNode(organizationNodeDTOMapper.toOrganizationNodeDTO(newOrganizationNode))
                .build();
    }
}
