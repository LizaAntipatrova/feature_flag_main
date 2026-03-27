package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.get.GetDescendantsOrganizationNodesRequest;
import org.redflag.dto.node.get.GetDescendantsOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetDescendantsOrganizationNodesService extends BaseService<GetDescendantsOrganizationNodesRequest, GetDescendantsOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;

    @Override
    protected GetDescendantsOrganizationNodesResponse execute(GetDescendantsOrganizationNodesRequest request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository
                .findAllDescendantsByIdAndDepth(request.getOrganizationId(), request.getNodeId(), request.getDepth());

        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }

        return toOrganizationNodesResponse(request, organizationNodes);
    }

    private GetDescendantsOrganizationNodesResponse toOrganizationNodesResponse(GetDescendantsOrganizationNodesRequest request, List<OrganizationNode> organizationNodes) {
        return GetDescendantsOrganizationNodesResponse.builder()
                .nodeId(request.getNodeId())
                .depth(request.getDepth())
                .items(organizationNodes.stream()
                        .map(organizationNodeDTOMapper::toOrganizationNodeDTO)
                        .toList())
                .build();
    }
}
