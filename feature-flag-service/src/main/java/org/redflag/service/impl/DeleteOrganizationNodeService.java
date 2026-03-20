package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.delete.DeleteOrganizationNodeRequest;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;

@Singleton
@RequiredArgsConstructor
public class DeleteOrganizationNodeService extends BaseService<DeleteOrganizationNodeRequest, Void> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected Void execute(DeleteOrganizationNodeRequest request) {
        organizationNodeRepository.deleteSubtree(request.nodeId());
        return null;
    }
}
