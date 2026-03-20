package org.redflag.dto.node.get;


public record GetDescendantsOrganizationNodesRequest(Long organizationId, Long nodeId, Integer depth) {

}
