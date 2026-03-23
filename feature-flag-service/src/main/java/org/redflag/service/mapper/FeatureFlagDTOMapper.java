package org.redflag.service.mapper;

import org.redflag.dto.featureflag.FeatureFlagDTO;
import org.redflag.model.FeatureFlag;

public class FeatureFlagDTOMapper {
    public static FeatureFlagDTO toFeatureFlagDTO(FeatureFlag featureFlag) {
        return FeatureFlagDTO.builder()
                .id(featureFlag.getId())
                .nodeId(featureFlag.getOrganizationNode().getId())
                .name(featureFlag.getName())
                .value(featureFlag.getValue())
                .version(featureFlag.getVersion())
                .build();
    }
}
