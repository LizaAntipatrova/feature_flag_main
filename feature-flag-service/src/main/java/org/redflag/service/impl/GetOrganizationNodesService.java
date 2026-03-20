package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.get.GetOrganizationNodesRequest;
import org.redflag.dto.node.get.GetOrganizationNodesResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.validator.PaginationParameterValidator;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetOrganizationNodesService extends BaseService<GetOrganizationNodesRequest, GetOrganizationNodesResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected void validateRequest(GetOrganizationNodesRequest request) {
        if (!PaginationParameterValidator.validateLimit(request.limit())) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }
        if (!PaginationParameterValidator.validateOffset(request.offset())) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }
    }

    @Override
    protected GetOrganizationNodesResponse execute(GetOrganizationNodesRequest request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findAllByOrganizationIdAndParentId(
                request.organizationId(),
                request.nodeId(),
                request.limit(),
                request.offset());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
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
