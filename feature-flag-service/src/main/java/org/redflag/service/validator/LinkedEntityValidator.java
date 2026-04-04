package org.redflag.service.validator;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.error.ErrorCatalog;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.repository.OrganizationNodeRepository;

@Singleton
@RequiredArgsConstructor
public class LinkedEntityValidator {

    private final OrganizationNodeRepository organizationNodeRepository;
    private final FeatureFlagRepository featureFlagRepository;

    public void checkIsNodeInOrganization(Long nodeId, Long organizationId) {
        if (!organizationNodeRepository.isNodeInOrganization(
                nodeId,
                organizationId)) {
            throw ErrorCatalog.NO_SUCH_NODE_IN_ORGANIZATION.getException();
        }
    }

    public void checkIsFeatureFlagInNode(Long featureFlagId, Long nodeId) {
        if (!featureFlagRepository.isFeatureFlagInNode(
                featureFlagId,
                nodeId)) {
            throw ErrorCatalog.NO_SUCH_FLAG_IN_NODE.getException();
        }
    }

    public void checkIsFeatureFlagInNodeByName(String flagName, Long nodeId) {
        if (!featureFlagRepository.isFeatureFlagInNodeByFlagName(
                flagName,
                nodeId)) {
            throw ErrorCatalog.NO_SUCH_FLAG_IN_NODE.getException();
        }
    }

}
