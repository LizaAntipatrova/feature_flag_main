package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.get.GetOrganizationNodeByIdRequest;
import org.redflag.dto.node.get.GetOrganizationNodeByIdResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;

import java.util.Optional;

@RequiredArgsConstructor
@Singleton
public class GetOrganizationNodeByIdService extends BaseService<GetOrganizationNodeByIdRequest, GetOrganizationNodeByIdResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected GetOrganizationNodeByIdResponse execute(GetOrganizationNodeByIdRequest request) {
        Optional<OrganizationNode> organizationNodeOpt =
                organizationNodeRepository.findByOrganization_IdAndId(request.organizationId(), request.nodeId());
        if (organizationNodeOpt.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        OrganizationNode organizationNode = organizationNodeOpt.get();
        return new GetOrganizationNodeByIdResponse(organizationNode.getId(),
                organizationNode.getOrganization().getId(),
                organizationNode.getUuid(),
                organizationNode.getPath(),
                organizationNode.getName(),
                organizationNode.getIsService(),
                organizationNode.getVersion());
    }
}
