package org.redflag.service.transaction;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.update.UpdateOrganizationNodeRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.mapper.OrganizationNodeWithCredentialsDTOMapper;
import org.redflag.service.validator.EntityVersionValidator;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class TransactionUpdateOrganizationNodeService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeWithCredentialsDTOMapper organizationNodeWithCredentialsDTOMapper;
    private final EntityVersionValidator entityVersionValidator;


    @Transactional
    public UpdateOrganizationNodeTransactionalResult updateNode(UpdateOrganizationNodeRequest request) {
        OrganizationNode organizationNode = organizationNodeRepository.findById(request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        entityVersionValidator.checkVersionMatch(organizationNode.getVersion(), request.getVersion());
        UpdateOrganizationNodeTransactionalResult.UpdateOrganizationNodeTransactionalResultBuilder result =
                UpdateOrganizationNodeTransactionalResult
                        .builder()
                        .nodeId(organizationNode.getId())
                        .oldName(organizationNode.getName())
                        .oldIsService(organizationNode.getIsService());
        organizationNode.setName(request.getName())
                .setIsService(request.getIsService());

        OrganizationNode newOrganizationNode;
        try {
            newOrganizationNode = organizationNodeRepository.update(organizationNode);
            organizationNodeRepository.flush();
        } catch (OptimisticLockException e) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }

        return result.versionAfterUpdate(newOrganizationNode.getVersion())
                .dto(organizationNodeWithCredentialsDTOMapper.toOrganizationNodeWithCredentialsDTO(newOrganizationNode))
                .build();
    }

    @Transactional
    public void compensateUpdateNode(UpdateOrganizationNodeTransactionalResult transactionalResult) {
        OrganizationNode currentNode = organizationNodeRepository.findById(transactionalResult.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        if (!Objects.equals(currentNode.getVersion(), transactionalResult.getVersionAfterUpdate())) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }

        currentNode.setName(transactionalResult.getOldName())
                .setIsService(transactionalResult.getOldIsService());

        try {
            organizationNodeRepository.update(currentNode);
            organizationNodeRepository.flush();
        } catch (OptimisticLockException e) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
    }
}
