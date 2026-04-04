package org.redflag.service.impl.node;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.update.MoveOrganizationNodeRequest;
import org.redflag.dto.node.update.MoveOrganizationNodeResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;
import org.redflag.service.util.LtreePathUtil;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.EntityVersionValidator;
import org.redflag.service.validator.LinkedEntityValidator;
import org.redflag.service.validator.OrganizationNodeValidator;

import java.util.List;
import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class MoveOrganizationNodeService extends BaseService<MoveOrganizationNodeRequest, MoveOrganizationNodeResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;
    private final OrganizationNodeValidator organizationNodeValidator;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;
    private final EntityVersionValidator entityVersionValidator;


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
        authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNewParentId(), request.getOrganizationId());
        organizationNodeValidator.checkNodeIsNotService(request.getNewParentId());

        OrganizationNode movedNode = organizationNodeRepository
                .findByOrganization_IdAndId(request.getOrganizationId(), request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (movedNode.getPath().equals(movedNode.getId().toString())) {
            throw ErrorCatalog.MOVE_ROOT_NODE.getException();
        }
        OrganizationNode parentNode = organizationNodeRepository
                .findByOrganization_IdAndId(request.getOrganizationId(), request.getNewParentId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (parentNode.getPath().contains(movedNode.getPath())) {
            throw ErrorCatalog.CYCLE_MOVE.getException();
        }
    }

    @Override
    @Transactional
    protected MoveOrganizationNodeResponse execute(MoveOrganizationNodeRequest request) {
        List<OrganizationNode> subtree = organizationNodeRepository
                .findSubtreeByOrganizationIdAndParentId(request.getOrganizationId(), request.getNodeId());

        OrganizationNode parentNode = organizationNodeRepository.findByOrganization_IdAndId(request.getOrganizationId(),
                        request.getNewParentId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        entityVersionValidator.checkVersionMatch(parentNode.getVersion(), request.getVersion());

        OrganizationNode rootNode = subtree.stream()
                .filter((node) -> node.getId().equals(request.getNodeId()))
                .findAny()
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        String oldRootPath = rootNode.getPath();
        LtreePathUtil.replaceSubtreeNodesParentPath(subtree, oldRootPath, parentNode.getPath());
        try {
            organizationNodeRepository.updateAll(subtree);
            organizationNodeRepository.flush();
        } catch (OptimisticLockException e) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }


        return toMoveOrganizationNodeResponse(request, rootNode, oldRootPath, subtree);
    }

    private MoveOrganizationNodeResponse toMoveOrganizationNodeResponse(MoveOrganizationNodeRequest request, OrganizationNode rootNode, String oldRootPath, List<OrganizationNode> subtree) {
        return MoveOrganizationNodeResponse.builder()
                .id(rootNode.getId())
                .uuid(rootNode.getUuid())
                .oldPath(oldRootPath)
                .newPath(rootNode.getPath())
                .movedDescendants(subtree.stream()
                        .filter((node) -> !node.getId().equals(request.getNodeId()))
                        .map(organizationNodeDTOMapper::toOrganizationNodeDTO)
                        .toList())
                .build();
    }

}
