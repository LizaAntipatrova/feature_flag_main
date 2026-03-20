package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.delete.DeleteFeatureFlagRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;

@Singleton
@RequiredArgsConstructor
public class DeleteFeatureFlagService extends BaseService<DeleteFeatureFlagRequest, Void> {
    private final FeatureFlagRepository featureFlagRepository;

    @Override
    protected Void execute(DeleteFeatureFlagRequest request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.flagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        featureFlagRepository.delete(featureFlag);
        return null;
    }
}
