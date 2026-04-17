package org.redflag.service.mapper;

import jakarta.inject.Singleton;
import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.model.FeatureFlag;
@Singleton
public class FeatureFlagDTOMapper {
    public FeatureFlagDTO toFeatureFlagDTO(FeatureFlag featureFlag) {
        return FeatureFlagDTO.builder()
                .id(featureFlag.getId())
                .nodeId(featureFlag.getOrganizationNode().getId())
                .name(featureFlag.getName())
                .value(featureFlag.getValue())
                .description(featureFlag.getDescription())
                .version(featureFlag.getVersion())
                .build();
    }
}
