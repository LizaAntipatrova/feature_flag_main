package org.redflag.service.validator;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.error.ErrorCatalog;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;

@Singleton
@RequiredArgsConstructor
public class UniqueNameValidator {
    private final OrganizationRepository organizationRepository;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final FeatureFlagRepository featureFlagRepository;

    public void checkIsUniqueOrganizationName(String organizationName) {
        if (organizationRepository.existsByName(organizationName)) {
            throw ErrorCatalog.NOT_UNIQUE_ORGANIZATION_NAME.getException();
        }
    }

    public void checkIsFeatureFlagNameMissingInOrganization(Long organizationId, String featureFlagName) {
        if (featureFlagRepository.existsByOrganizationIdAndName(
                organizationId,
                featureFlagName)) {
            throw ErrorCatalog.NOT_UNIQUE_FEATURE_FLAG_NAME_IN_ORGANIZATION.getException();
        }
    }

    public void checkIsNodeNameMissingInOrganization(Long organizationId, String nodeName) {
        if (organizationNodeRepository.existsByOrganization_IdAndName(organizationId, nodeName)) {
            throw ErrorCatalog.NOT_UNIQUE_ORGANIZATION_NODE_NAME_IN_ORGANIZATION.getException();
        }
    }
}