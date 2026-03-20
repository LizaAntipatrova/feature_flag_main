package org.redflag.dto.featureflag.get;

public record GetLinkedFeatureFlagsRequest(Long organizationId, Long nodeId, RelationType relation, Integer limit,
                                           Integer offset) {
}
