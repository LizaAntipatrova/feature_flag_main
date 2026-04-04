package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;

@RequiredArgsConstructor
@Singleton
public class GetOrganizationNodeByIdService extends BaseService<OrganizationNodeIdDTO, OrganizationNodeDTO> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;

    @Override
    protected void validateState(OrganizationNodeIdDTO request) {
        authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
    }

    @Override
    protected OrganizationNodeDTO execute(OrganizationNodeIdDTO request) {
        OrganizationNode organizationNode = organizationNodeRepository
                .findByOrganization_IdAndId(request.getOrganizationId(), request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);

        return organizationNodeDTOMapper.toOrganizationNodeDTO(organizationNode);
    }
}
