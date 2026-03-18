package org.redflag.dto.featureflag.get;

import org.redflag.service.AbstractService;

public record GetFeatureFlagsRequest(Long organizationId, Long nodeId, Integer limit, Integer offset) {
}
