package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.complex.CreateOrganizationWithRootNodeResponse;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.create.CreateOrganizationNodeRequest;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.dto.organization.create.CreateOrganizationRequest;
import org.redflag.service.BaseService;
import org.redflag.service.impl.node.CreateOrganizationNodeService;
import org.redflag.service.impl.organization.CreateOrganizationService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;

@RequiredArgsConstructor
@Singleton
public class CreateOrganizationWithRootNodesService extends BaseService<CreateOrganizationRequest, CreateOrganizationWithRootNodeResponse> {
    private final CreateOrganizationNodeService createOrganizationNodeService;
    private final CreateOrganizationService createOrganizationService;

    @Override
    protected CreateOrganizationWithRootNodeResponse execute(CreateOrganizationRequest request) {
        OrganizationDTO organizationDTO = createOrganizationService
                .service(request);

        CreateOrganizationNodeRequest createOrganizationNodeRequest = CreateOrganizationNodeRequest
                .builder()
                .isService(false)
                .parentId(null)
                .name(request.getName()).build();

        createOrganizationNodeRequest.setOrganizationId(organizationDTO.getId());
        OrganizationNodeDTO newOrganizationNodeDTO = createOrganizationNodeService
                .service(createOrganizationNodeRequest);

        return toCreateOrganizationWithRootNodeResponse(organizationDTO, newOrganizationNodeDTO);
    }

    private static CreateOrganizationWithRootNodeResponse toCreateOrganizationWithRootNodeResponse(
            OrganizationDTO organizationDTO,
            OrganizationNodeDTO newOrganizationNodeDTO
    ) {
        return CreateOrganizationWithRootNodeResponse.builder()
                .id(organizationDTO.getId())
                .name(organizationDTO.getName())
                .organizationNode(newOrganizationNodeDTO)
                .build();
    }
}
