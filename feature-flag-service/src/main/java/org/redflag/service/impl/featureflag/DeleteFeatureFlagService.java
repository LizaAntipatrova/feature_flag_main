package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.FeatureFlagIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;

@Singleton
@RequiredArgsConstructor
public class DeleteFeatureFlagService extends BaseService<FeatureFlagIdDTO, Void> {
    private final FeatureFlagRepository featureFlagRepository;

    @Override
    protected Void execute(FeatureFlagIdDTO request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.getFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        featureFlagRepository.delete(featureFlag);
        return null;
    }
}
