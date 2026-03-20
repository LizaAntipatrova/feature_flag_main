package org.redflag.dto.featureflag.get;

public record GetFeatureFlagsRequest(Long organizationId, Long nodeId, Integer limit, Integer offset) {
}
