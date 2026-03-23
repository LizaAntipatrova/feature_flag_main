package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;

@Singleton
@RequiredArgsConstructor
public class DeleteOrganizationNodeService extends BaseService<OrganizationNodeIdDTO, Void> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected Void execute(OrganizationNodeIdDTO request) {
        organizationNodeRepository.deleteSubtree(request.getNodeId());
        return null;
    }
}
