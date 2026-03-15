package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.get.GetOrganizationNodesRequest;
import org.redflag.dto.node.get.GetOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.AbstractService;

import java.util.List;
import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class GetOrganizationNodesService extends AbstractService<GetOrganizationNodesRequest, GetOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected void validateRequest(GetOrganizationNodesRequest request) {
        Integer limit = request.limit();
        Integer offset = request.offset();
        //TODO: вынести максимальный лимит и прописать в api
        if (Objects.isNull(limit) || limit <= 0 || limit > 100) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }

        if (Objects.isNull(offset) || offset < 0) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }
    }

    @Override
    protected GetOrganizationNodesResponse logic(GetOrganizationNodesRequest request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findAllByOrganizationIdAndParentId(
                request.organizationId(),
                request.nodeId(),
                request.limit(),
                request.offset());
        return new GetOrganizationNodesResponse(
                organizationNodes.stream()
                        .map(this::toOrganizationNodeDTO)
                        .toList(),
                request.limit(),
                request.offset(),
                organizationNodes.size());
    }

    private GetOrganizationNodesResponse.OrganizationNodeDTO toOrganizationNodeDTO(OrganizationNode organizationNode) {
        return new GetOrganizationNodesResponse.OrganizationNodeDTO(organizationNode.getId(),
                organizationNode.getOrganization().getId(),
                organizationNode.getUuid(),
                organizationNode.getPath(),
                organizationNode.getName(),
                organizationNode.getIsService(),
                organizationNode.getVersion());
    }
}
