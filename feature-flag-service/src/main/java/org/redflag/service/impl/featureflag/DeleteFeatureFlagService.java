package org.redflag.service.impl.featureflag;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.FeatureFlagIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;

@Singleton
@RequiredArgsConstructor
public class DeleteFeatureFlagService extends BaseService<FeatureFlagIdDTO, Void> {
    private final FeatureFlagRepository featureFlagRepository;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;

    @Override
    protected void validateState(FeatureFlagIdDTO request) {
        authRightsToNodeValidator.checkIsAuthNodeIsParentToRequestNode(request.getNodeId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
        linkedEntityValidator.checkIsFeatureFlagInNode(request.getFlagId(), request.getNodeId());
    }

    @Override
    @Transactional
    protected Void execute(FeatureFlagIdDTO request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.getFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        featureFlagRepository.delete(featureFlag);
        return null;
    }
}
