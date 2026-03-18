package org.redflag.dto.featureflag.get;

public record GetFeatureFlagByNameRequest(Long organizationId, Long nodeId, String flagName) {

}
