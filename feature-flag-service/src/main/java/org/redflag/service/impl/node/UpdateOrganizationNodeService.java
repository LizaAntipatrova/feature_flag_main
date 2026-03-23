package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.update.UpdateOrganizationNodeRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class UpdateOrganizationNodeService extends BaseService<UpdateOrganizationNodeRequest, OrganizationNodeDTO> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected void validateRequest(UpdateOrganizationNodeRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("name");
        }
        if (Objects.isNull(request.getIsService())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("isService");
        }
        if (Objects.isNull(request.getVersion())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("version");
        }
    }

    @Override
    protected void validateState(UpdateOrganizationNodeRequest request) {
        OrganizationNode organizationNode = organizationNodeRepository
                .findByOrganization_IdAndId(request.getOrganizationId(), request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (!organizationNode.getVersion().equals(request.getVersion())) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
        if (!organizationNode.getName().equals(request.getName())
        && organizationNodeRepository.existsByOrganization_IdAndName(request.getOrganizationId(), request.getName())) {
            throw ErrorCatalog.NOT_UNIQUE_ORGANIZATION_NODE_NAME_IN_ORGANIZATION.getException();
        }
        if (request.getIsService() && organizationNodeRepository.existsDescendants(request.getNodeId())) {
            throw ErrorCatalog.SERVICE_CANNOT_HAVE_DESCENDANTS.getException();
        }

    }

    @Override
    protected OrganizationNodeDTO execute(UpdateOrganizationNodeRequest request) {
        OrganizationNode organizationNode = organizationNodeRepository.findById(request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        if (!organizationNode.getVersion().equals(request.getVersion())) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }

        organizationNode.setName(request.getName())
                .setIsService(request.getIsService());

        OrganizationNode newOrganizationNode;
        try {
            newOrganizationNode = organizationNodeRepository
                    .update(organizationNode);
        } catch (OptimisticLockException e) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }

        return OrganizationNodeDTOMapper.toOrganizationNodeDTO(newOrganizationNode);
    }
}
