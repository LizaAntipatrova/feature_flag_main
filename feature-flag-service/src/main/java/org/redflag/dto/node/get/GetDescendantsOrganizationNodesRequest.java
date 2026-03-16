package org.redflag.dto.node.get;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.service.AbstractService;


public record GetDescendantsOrganizationNodesRequest(Long organizationId, Long nodeId, Integer depth) {

}
