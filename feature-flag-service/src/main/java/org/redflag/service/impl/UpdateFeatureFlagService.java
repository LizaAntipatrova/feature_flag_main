package org.redflag.service.impl;

import jakarta.inject.Singleton;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.update.UpdateFeatureFlagRequest;
import org.redflag.dto.featureflag.update.UpdateFeatureFlagResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.AbstractService;

import java.util.Objects;
@Singleton
@RequiredArgsConstructor
public class UpdateFeatureFlagService extends AbstractService<UpdateFeatureFlagRequest, UpdateFeatureFlagResponse> {
    private final FeatureFlagRepository featureFlagRepository;

    @Override
    protected void validateRequest(UpdateFeatureFlagRequest request) {
        if (Objects.isNull(request.getValue())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("value");
        }
        if (Objects.isNull(request.getVersion())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("version");
        }
    }

    @Override
    protected void validateState(UpdateFeatureFlagRequest request) {
        FeatureFlag featureFlag = featureFlagRepository
                .findById(request.getFeatureFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (!featureFlag.getVersion().equals(request.getVersion())) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
    }

    @Override
    protected UpdateFeatureFlagResponse logic(UpdateFeatureFlagRequest request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.getFeatureFlagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (!featureFlag.getVersion().equals(request.getVersion())) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
        featureFlag
                .setValue(request.getValue());
        FeatureFlag newFeatureFlag;
        try {
            newFeatureFlag = featureFlagRepository
                    .update(featureFlag);
        } catch (OptimisticLockException e) {
            throw ErrorCatalog.OPTIMISTIC_LOCK.getException();
        }
        return new UpdateFeatureFlagResponse(
                newFeatureFlag.getId(),
                newFeatureFlag.getOrganizationNode().getId(),
                newFeatureFlag.getName(),
                newFeatureFlag.getValue(),
                newFeatureFlag.getVersion());
    }
}
