package org.redflag.dto.node.get;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;


public record GetSubtreeNodeOrganizationsRequest(Long organizationId, Long nodeId) {

}
