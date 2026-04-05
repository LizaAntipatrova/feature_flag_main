package org.redflag.service.transaction;

import lombok.Builder;
import lombok.Getter;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.OrganizationNodeWithCredentialsDTO;

import java.util.UUID;
@Builder
@Getter
public class UpdateOrganizationNodeTransactionalResult {
    private final Long nodeId;
    private final String oldName;
    private final Boolean oldIsService;
    private final Long versionAfterUpdate;
    private final OrganizationNodeWithCredentialsDTO dto;

}
