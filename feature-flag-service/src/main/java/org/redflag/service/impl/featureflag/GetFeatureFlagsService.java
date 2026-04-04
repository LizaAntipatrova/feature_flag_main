package org.redflag.service.impl.featureflag;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.featureflag.get.GetFeatureFlagsRequest;
import org.redflag.dto.featureflag.get.GetFeatureFlagsResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;
import org.redflag.service.validator.PaginationParameterValidator;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagsService extends BaseService<GetFeatureFlagsRequest, GetFeatureFlagsResponse> {
    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureFlagDTOMapper featureFlagDTOMapper;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;
    private final PaginationParameterValidator paginationParameterValidator;

    @Override
    protected void validateRequest(GetFeatureFlagsRequest request) {
        paginationParameterValidator.validateLimit(request.getLimit());
        paginationParameterValidator.validateOffset(request.getOffset());
    }

    @Override
    protected void validateState(GetFeatureFlagsRequest request) {
        authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
    }

    @Override
    protected GetFeatureFlagsResponse execute(GetFeatureFlagsRequest request) {
        List<FeatureFlag> featureFlags = featureFlagRepository
                .findByOrganizationNodeId(request.getNodeId(), request.getLimit(), request.getOffset());
        if (featureFlags.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }

        return toGetFeatureFlagsResponse(request, featureFlags);
    }

    private GetFeatureFlagsResponse toGetFeatureFlagsResponse(GetFeatureFlagsRequest request, List<FeatureFlag> featureFlags) {
        return GetFeatureFlagsResponse.builder()
                .nodeId(request.getNodeId())
                .items(featureFlags.stream().map(featureFlagDTOMapper::toFeatureFlagDTO).toList())
                .limit(request.getLimit())
                .offset(request.getOffset())
                .total(featureFlags.size())
                .build();
    }


}
