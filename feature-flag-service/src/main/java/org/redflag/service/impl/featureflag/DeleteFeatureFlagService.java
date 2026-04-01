package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.featureflag.FeatureFlagIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;

@Singleton
@RequiredArgsConstructor
public class DeleteFeatureFlagService extends BaseService<FeatureFlagIdDTO, Void> {
    private final FeatureFlagRepository featureFlagRepository;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateState(FeatureFlagIdDTO request) {
        if (!organizationNodeRepository.existsChildNodeInParentNodeByChildIdAndParentUuid(
                request.getNodeId(),
                authenticationProvider.getAuthenticationNodeUuid()
        )){
            throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
        }
        if (!organizationNodeRepository.isNodeInOrganization(
                request.getNodeId(),
                request.getOrganizationId())){
            throw ErrorCatalog.NO_SUCH_NODE_IN_ORGANIZATION.getException();
        }
        if (!featureFlagRepository.isFeatureFlagInNode(
                request.getFlagId(),
                request.getNodeId())){
            throw ErrorCatalog.NO_SUCH_FLAG_IN_NODE.getException();
        }
    }

    @Override
    protected Void execute(FeatureFlagIdDTO request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.getFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        featureFlagRepository.delete(featureFlag);
        return null;
    }
}
