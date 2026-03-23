package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.update.MoveOrganizationNodeRequest;
import org.redflag.dto.node.update.MoveOrganizationNodeResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;
import org.redflag.service.util.LtreePathUtil;

import java.util.List;
import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class MoveOrganizationNodeService extends BaseService<MoveOrganizationNodeRequest, MoveOrganizationNodeResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected void validateRequest(MoveOrganizationNodeRequest request) {
        if (Objects.isNull(request.getVersion())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("version");
        }
        if (Objects.isNull(request.getNewParentId())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("newParentId");
        }
    }

    @Override
    protected void validateState(MoveOrganizationNodeRequest request) {
        OrganizationNode movedNode = organizationNodeRepository
                .findByOrganization_IdAndId(request.getOrganizationId(), request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (!movedNode.getVersion().equals(request.getVersion())) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
        if (movedNode.getPath().equals(movedNode.getId().toString())) {
            throw ErrorCatalog.MOVE_ROOT_NODE.getException();
        }
        OrganizationNode parentNode = organizationNodeRepository
                .findByOrganization_IdAndId(request.getOrganizationId(), request.getNewParentId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (parentNode.getPath().contains(movedNode.getPath())) {
            throw ErrorCatalog.CYCLE_MOVE.getException();
        }
        if (parentNode.getIsService()) {
            throw ErrorCatalog.SERVICE_CANNOT_HAVE_DESCENDANTS.getException();
        }
    }

    @Override
    protected MoveOrganizationNodeResponse execute(MoveOrganizationNodeRequest request) {
        List<OrganizationNode> subtree = organizationNodeRepository
                .findSubtreeByOrganizationIdAndParentId(request.getOrganizationId(), request.getNodeId());

        OrganizationNode parentNode = organizationNodeRepository.findByOrganization_IdAndId(request.getOrganizationId(),
                request.getNewParentId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        OrganizationNode rootNode = subtree.stream()
                .filter((node) -> node.getId().equals(request.getNodeId()))
                .findAny()
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        String oldRootPath = rootNode.getPath();
        LtreePathUtil.replaceSubtreeNodesParentPath(subtree, oldRootPath, parentNode.getPath());

        organizationNodeRepository.updateAll(subtree);

        return toMoveOrganizationNodeResponse(request, rootNode, oldRootPath, subtree);
    }

    private static MoveOrganizationNodeResponse toMoveOrganizationNodeResponse(MoveOrganizationNodeRequest request, OrganizationNode rootNode, String oldRootPath, List<OrganizationNode> subtree) {
        return MoveOrganizationNodeResponse.builder()
                .id(rootNode.getId())
                .uuid(rootNode.getUuid())
                .oldPath(oldRootPath)
                .newPath(rootNode.getPath())
                .movedDescendants(subtree.stream()
                        .filter((node) -> !node.getId().equals(request.getNodeId()))
                        .map(OrganizationNodeDTOMapper::toOrganizationNodeDTO)
                        .toList())
                .build();
    }

}
