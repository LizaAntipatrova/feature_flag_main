package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.get.GetFeatureFlagByNameRequest;
import org.redflag.dto.featureflag.get.GetFeatureFlagByNameResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.AbstractService;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagByNameService extends AbstractService<GetFeatureFlagByNameRequest, GetFeatureFlagByNameResponse> {
    private final FeatureFlagRepository featureFlagRepository;

    @Override
    protected void validateRequest(GetFeatureFlagByNameRequest request) {
        String name = request.flagName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("flagName");
        }
    }

    @Override
    protected GetFeatureFlagByNameResponse logic(GetFeatureFlagByNameRequest request) {
        FeatureFlag featureFlag = featureFlagRepository.findByName(request.flagName())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return new GetFeatureFlagByNameResponse(featureFlag.getId(),
                featureFlag.getOrganizationNode().getId(),
                featureFlag.getName(),
                featureFlag.getValue(),
                featureFlag.getVersion());
    }
}
