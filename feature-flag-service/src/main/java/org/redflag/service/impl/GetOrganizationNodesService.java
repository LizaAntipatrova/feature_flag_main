package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.OrganizationNodeDTO;
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
        if (!PaginationParameterValidator.validateLimit(request.getLimit())) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }
        if (!PaginationParameterValidator.validateOffset(request.getOffset())) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }
    }

    @Override
    protected GetOrganizationNodesResponse execute(GetOrganizationNodesRequest request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findAllByOrganizationIdAndParentId(
                request.getOrganizationId(),
                request.getNodeId(),
                request.getLimit(),
                request.getOffset());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return GetOrganizationNodesResponse.builder()
                .items(organizationNodes.stream()
                        .map(this::toOrganizationNodeDTO)
                        .toList())
                .limit(request.getLimit())
                .offset(request.getOffset())
                .total(organizationNodes.size())
                .build();
    }

    private OrganizationNodeDTO toOrganizationNodeDTO(OrganizationNode organizationNode) {
        return OrganizationNodeDTO.builder()
                .id(organizationNode.getId())
                .organizationId(organizationNode.getOrganization().getId())
                .uuid(organizationNode.getUuid())
                .path(organizationNode.getPath())
                .name(organizationNode.getName())
                .isService(organizationNode.getIsService())
                .version(organizationNode.getVersion())
                .build();
    }
}
