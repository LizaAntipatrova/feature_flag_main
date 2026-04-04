package org.redflag.service.validator;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;

import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class OrganizationNodeValidator {
    private final OrganizationNodeRepository organizationNodeRepository;

    public void checkMissingRootNodeInOrganization(Long organizationId) {
        if (organizationNodeRepository.existsRootNodeInOrganization(organizationId)) {
            throw ErrorCatalog.ORGANIZATION_CAN_HAVE_ONE_ROOT_NODE.getException();
        }
    }

    public void checkNodeIsNotService(Long parentId) {
        OrganizationNode parentNode = organizationNodeRepository.findById(parentId)
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (parentNode.getIsService()) {
            throw ErrorCatalog.SERVICE_CANNOT_HAVE_DESCENDANTS.getException();
        }
    }

    public void checkNodeIsService(UUID nodeUuid) {
        OrganizationNode organizationNode = organizationNodeRepository.findByUuid(nodeUuid)
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (!organizationNode.getIsService()) {
            throw ErrorCatalog.NODE_MUST_BE_SERVICE.getException();
        }
    }

}
