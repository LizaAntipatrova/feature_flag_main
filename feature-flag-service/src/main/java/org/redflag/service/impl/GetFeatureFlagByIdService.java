package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.get.GetFeatureFlagByIdResponse;
import org.redflag.dto.featureflag.get.GetFeatureFlagByIdRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.AbstractService;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagByIdService extends AbstractService<GetFeatureFlagByIdRequest, GetFeatureFlagByIdResponse> {
    private final FeatureFlagRepository featureFlagRepository;

    @Override
    protected GetFeatureFlagByIdResponse logic(GetFeatureFlagByIdRequest request) {
        FeatureFlag featureFlag = featureFlagRepository.findById(request.flagId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        return new GetFeatureFlagByIdResponse(featureFlag.getId(),
                featureFlag.getOrganizationNode().getId(),
                featureFlag.getName(),
                featureFlag.getValue(),
                featureFlag.getVersion());
    }
}
