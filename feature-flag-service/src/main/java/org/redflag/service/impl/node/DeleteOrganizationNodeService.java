package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.auth.NodeUuidsDTO;
import org.redflag.dto.auth.SdkUuidsDTO;
import org.redflag.dto.node.OrganizationNodeIdDTO;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.client.AuthClientService;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;

import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class DeleteOrganizationNodeService extends BaseService<OrganizationNodeIdDTO, Void> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;
    private final AuthClientService authClientService;

    @Override
    protected void validateState(OrganizationNodeIdDTO request) {
        authRightsToNodeValidator.checkIsAuthNodeIsParentToRequestNode(request.getNodeId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());
    }

    @Override
    protected Void execute(OrganizationNodeIdDTO request) {
        List<OrganizationNode> organizationNodes = organizationNodeRepository.findSubtreeByOrganizationIdAndParentId(
                request.getOrganizationId(),
                request.getNodeId()
        );
        try {
            List<UUID> nodeUuids = organizationNodes.stream().map(OrganizationNode::getUuid).toList();
            NodeUuidsDTO dtoForDeleteClients = NodeUuidsDTO.builder()
                    .nodeUuids(nodeUuids).build();
            authClientService.deleteClientsByNodeUuids(dtoForDeleteClients);
            List<UUID> serviceNodeUuids = organizationNodes.stream()
                    .filter(OrganizationNode::getIsService)
                    .map(OrganizationNode::getUuid)
                    .toList();

            if (!serviceNodeUuids.isEmpty()){
                SdkUuidsDTO dtoForDeleteServiceCredentials = SdkUuidsDTO.builder()
                        .nodeUuids(serviceNodeUuids).build();
                authClientService.deleteServiceCredentialsByNodeUuids(dtoForDeleteServiceCredentials);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw ErrorCatalog.AUTH_SERVICE_ERROR.getException();

        }

        organizationNodeRepository.deleteSubtree(request.getNodeId());
        return null;
    }
}
