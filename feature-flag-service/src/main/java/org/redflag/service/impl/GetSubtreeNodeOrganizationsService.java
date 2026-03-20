package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.get.GetSubtreeNodeOrganizationsRequest;
import org.redflag.dto.node.get.GetSubtreeNodeOrganizationsResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class GetSubtreeNodeOrganizationsService extends BaseService<GetSubtreeNodeOrganizationsRequest, GetSubtreeNodeOrganizationsResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected GetSubtreeNodeOrganizationsResponse execute(GetSubtreeNodeOrganizationsRequest request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findSubtreeByOrganizationIdAndParentId(
                request.organizationId(),
                request.nodeId());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return assembleOrganizationNodeSubtree(organizationNodes);
    }

    private GetSubtreeNodeOrganizationsResponse assembleOrganizationNodeSubtree(List<OrganizationNode> organizationNodes) {
        Map<String, GetSubtreeNodeOrganizationsResponse> subtreeNodeDTOS = organizationNodes.stream()
                .map(this::toGetSubtreeNodeOrganizationsResponse)
                .collect(Collectors.toMap(GetSubtreeNodeOrganizationsResponse::getPath, (nodeDTO) -> nodeDTO));

        GetSubtreeNodeOrganizationsResponse root = null;
        for (GetSubtreeNodeOrganizationsResponse node : subtreeNodeDTOS.values()) {
            //TODO: вынести работу с path
            int index = node.getPath().lastIndexOf('.');
            String parentPath = (index < 0) ? null : node.getPath().substring(0, index);

            GetSubtreeNodeOrganizationsResponse parentNode = subtreeNodeDTOS.get(parentPath);
            if (parentNode == null) {
                root = node;
            } else {
                parentNode.getChildren().add(node);
            }
        }
        return root;
    }

    private GetSubtreeNodeOrganizationsResponse toGetSubtreeNodeOrganizationsResponse(OrganizationNode organizationNode) {
        return new GetSubtreeNodeOrganizationsResponse(organizationNode.getId(),
                organizationNode.getOrganization().getId(),
                organizationNode.getUuid(),
                organizationNode.getPath(),
                organizationNode.getName(),
                organizationNode.getIsService(),
                organizationNode.getVersion(),
                new ArrayList<>());
    }
}
