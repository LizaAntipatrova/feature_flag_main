package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.complex.CreateOrganizationWithRootNodeResponse;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.create.CreateOrganizationNodeRequest;
import org.redflag.dto.organization.OrganizationDTO;
import org.redflag.dto.organization.create.CreateOrganizationRequest;
import org.redflag.service.BaseService;

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
        OrganizationNodeDTO createNodeResponse = createOrganizationNodeService
                .service(createOrganizationNodeRequest);

        return CreateOrganizationWithRootNodeResponse.builder()
                .id(organizationDTO.getId())
                .name(organizationDTO.getName())
                .organizationNode(
                        OrganizationNodeDTO.builder()
                        .id(createNodeResponse.getId())
                        .organizationId(createNodeResponse.getOrganizationId())
                        .uuid(createNodeResponse.getUuid())
                        .path(createNodeResponse.getPath())
                        .name(createNodeResponse.getName())
                        .isService(createNodeResponse.getIsService())
                        .version(createNodeResponse.getVersion())
                        .build())
                .build();
    }
}
