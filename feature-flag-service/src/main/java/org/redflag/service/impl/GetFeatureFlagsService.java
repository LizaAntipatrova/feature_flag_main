package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.get.GetFeatureFlagsRequest;
import org.redflag.dto.featureflag.get.GetFeatureFlagsResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.AbstractService;

import java.util.List;
import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagsService extends AbstractService<GetFeatureFlagsRequest, GetFeatureFlagsResponse> {
    private final FeatureFlagRepository featureFlagRepository;
    @Override
    protected void validateRequest(GetFeatureFlagsRequest request) {
        Integer limit = request.limit();
        Integer offset = request.offset();
        //TODO: вынести максимальный лимит и прописать в api
        if (Objects.isNull(limit) || limit <= 0 || limit > 100) {
            throw ErrorCatalog.BAD_LIMIT.getException();
        }
        if (Objects.isNull(offset) || offset < 0) {
            throw ErrorCatalog.BAD_OFFSET.getException();
        }
    }

    @Override
    protected GetFeatureFlagsResponse logic(GetFeatureFlagsRequest request) {
        List<FeatureFlag> featureFlags =  featureFlagRepository
                .findByOrganizationNode_Id(request.nodeId(), request.limit(),  request.offset());
        if (featureFlags.isEmpty()){
            throw ErrorCatalog.NO_DATA.getException();
        }

        return new GetFeatureFlagsResponse(request.nodeId(),
                featureFlags.stream().map(this::toFeatureFlag).toList(),
                request.limit(),
                request.offset(),
                featureFlags.size());
    }

    private GetFeatureFlagsResponse.FeatureFlag toFeatureFlag(FeatureFlag featureFlag){
        return new GetFeatureFlagsResponse.FeatureFlag(featureFlag.getId(),
                featureFlag.getOrganizationNode().getId(),
                featureFlag.getName(),
                featureFlag.getValue(),
                featureFlag.getVersion());
    }


}
