package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.complex.GetFeatureFlagsByUuidServiceNodeResponse;
import org.redflag.dto.complex.OrganizationNodeUuidDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.FeatureFlag;
import org.redflag.repository.FeatureFlagRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.FeatureFlagDTOMapper;
import org.redflag.service.validator.OrganizationNodeValidator;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class GetFeatureFlagsByUuidServiceNodeService extends BaseService<OrganizationNodeUuidDTO, GetFeatureFlagsByUuidServiceNodeResponse> {
    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureFlagDTOMapper featureFlagDTOMapper;
    private final OrganizationNodeValidator organizationNodeValidator;

    @Override
    protected void validateRequest(OrganizationNodeUuidDTO request) {
        UUID uuid = request.getNodeUuid();
        if (Objects.isNull(uuid)) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("uuid");
        }
    }

    @Override
    protected void validateState(OrganizationNodeUuidDTO request) {
        organizationNodeValidator.checkNodeIsService(request.getNodeUuid());
    }

    @Override
    protected GetFeatureFlagsByUuidServiceNodeResponse execute(OrganizationNodeUuidDTO request) {
        List<FeatureFlag> featureFlags = featureFlagRepository
                .findAllAncestorsFeatureFlagsByOrganizationNodeUuid(request.getNodeUuid());
        if (featureFlags.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        return toResponse(request, featureFlags);
    }

    private GetFeatureFlagsByUuidServiceNodeResponse toResponse(OrganizationNodeUuidDTO request, List<FeatureFlag> featureFlags) {
        return GetFeatureFlagsByUuidServiceNodeResponse.builder()
                .nodeUuid(request.getNodeUuid())
                .items(featureFlags.stream().map(featureFlagDTOMapper::toFeatureFlagDTO)
                        .toList())
                .total(featureFlags.size())
                .build();
    }


}
