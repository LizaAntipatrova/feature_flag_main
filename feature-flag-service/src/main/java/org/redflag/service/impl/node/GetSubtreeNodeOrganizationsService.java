package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.dto.node.get.GetSubtreeNodeOrganizationsResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.util.LtreePathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class GetSubtreeNodeOrganizationsService extends BaseService<OrganizationNodeIdDTO, GetSubtreeNodeOrganizationsResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateState(OrganizationNodeIdDTO request) {
        if (!organizationNodeRepository.isNodeInOrganization(
                authenticationProvider.getAuthenticationNodeUuid(),
                request.getOrganizationId())){
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
        if (!organizationNodeRepository.isNodeInOrganization(
                request.getNodeId(),
                request.getOrganizationId())){
            throw ErrorCatalog.NO_SUCH_NODE_IN_ORGANIZATION.getException();
        }
    }

    @Override
    protected GetSubtreeNodeOrganizationsResponse execute(OrganizationNodeIdDTO request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findSubtreeByOrganizationIdAndParentId(
                request.getOrganizationId(),
                request.getNodeId());
        if (organizationNodes.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return assembleOrganizationNodeSubtree(organizationNodes);
    }

    private GetSubtreeNodeOrganizationsResponse assembleOrganizationNodeSubtree(List<OrganizationNode> organizationNodes) {
        Map<String, GetSubtreeNodeOrganizationsResponse> subtreeNodeDTOS = organizationNodes.stream()
                .map(this::toGetSubtreeNodeOrganizationsResponse)
                .collect(Collectors.toMap(
                        GetSubtreeNodeOrganizationsResponse::getPath,
                        (nodeDTO) -> nodeDTO)
                );

        GetSubtreeNodeOrganizationsResponse root = null;
        for (GetSubtreeNodeOrganizationsResponse node : subtreeNodeDTOS.values()) {
            String parentPath = LtreePathUtil.getParentPath(node.getPath());

            if (Objects.isNull(parentPath) || Objects.isNull(subtreeNodeDTOS.get(parentPath))) {
                root = node;
            } else {
                subtreeNodeDTOS.get(parentPath).getChildren().add(node);
            }
        }
        return root;
    }

    private GetSubtreeNodeOrganizationsResponse toGetSubtreeNodeOrganizationsResponse(OrganizationNode organizationNode) {
        return GetSubtreeNodeOrganizationsResponse.builder()
                .id(organizationNode.getId())
                .name(organizationNode.getName())
                .path(organizationNode.getPath())
                .isService(organizationNode.getIsService())
                .uuid(organizationNode.getUuid())
                .version(organizationNode.getVersion())
                .children(new ArrayList<>()).build();
    }
}
