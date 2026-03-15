package org.redflag.dto.node.get;

public record GetOrganizationNodesRequest(Long organizationId, Long nodeId, Integer limit, Integer offset){
}
