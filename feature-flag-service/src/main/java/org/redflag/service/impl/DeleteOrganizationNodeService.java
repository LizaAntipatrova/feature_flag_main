package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.delete.DeleteOrganizationNodeRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.AbstractService;

@Singleton
@RequiredArgsConstructor
public class DeleteOrganizationNodeService extends AbstractService<DeleteOrganizationNodeRequest, Void> {
    private final OrganizationNodeRepository organizationNodeRepository;

    @Override
    protected void validateState(DeleteOrganizationNodeRequest request) {
        if(!organizationNodeRepository.existsByOrganization_IdAndId(request.organizationId(), request.nodeId())){

        }
    }

    @Override
    protected Void logic(DeleteOrganizationNodeRequest request) {
        organizationNodeRepository.deleteSubtree(request.nodeId());
        return null;
    }
}
