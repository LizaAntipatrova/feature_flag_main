package org.redflag.dto.featureflag.delete;

public record DeleteFeatureFlagRequest(Long organizationId, Long nodeId, Long flagId) {
}
