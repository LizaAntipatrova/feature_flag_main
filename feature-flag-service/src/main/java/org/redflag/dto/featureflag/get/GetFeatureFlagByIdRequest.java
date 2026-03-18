package org.redflag.dto.featureflag.get;

public record GetFeatureFlagByIdRequest(Long organizationId, Long nodeId, Long flagId) {
}
