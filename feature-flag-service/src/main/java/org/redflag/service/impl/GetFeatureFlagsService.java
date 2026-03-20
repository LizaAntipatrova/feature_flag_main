package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.get.GetFeatureFlagsRequest;
import org.redflag.dto.featureflag.get.GetFeatureFlagsResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;
import org.redflag.validator.PaginationParameterValidator;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagsService extends BaseService<GetFeatureFlagsRequest, GetFeatureFlagsResponse> {
    private final FeatureFlagRepository featureFlagRepository;

    @Override
    protected void validateRequest(GetFeatureFlagsRequest request) {
        if (!PaginationParameterValidator.validateLimit(request.limit())) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }
        if (!PaginationParameterValidator.validateOffset(request.offset())) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }
    }

    @Override
    protected GetFeatureFlagsResponse execute(GetFeatureFlagsRequest request) {
        List<FeatureFlag> featureFlags = featureFlagRepository
                .findByOrganizationNodeId(request.nodeId(), request.limit(), request.offset());
        if (featureFlags.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }

        return new GetFeatureFlagsResponse(request.nodeId(),
                featureFlags.stream().map(this::toFeatureFlag).toList(),
                request.limit(),
                request.offset(),
                featureFlags.size());
    }

    private GetFeatureFlagsResponse.FeatureFlag toFeatureFlag(FeatureFlag featureFlag) {
        return new GetFeatureFlagsResponse.FeatureFlag(featureFlag.getId(),
                featureFlag.getOrganizationNode().getId(),
                featureFlag.getName(),
                featureFlag.getValue(),
                featureFlag.getVersion());
    }


}
