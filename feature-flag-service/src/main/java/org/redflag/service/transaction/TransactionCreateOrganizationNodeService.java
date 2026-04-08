package org.redflag.service.transaction;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.OrganizationNodeWithCredentialsDTO;
import org.redflag.dto.node.create.CreateOrganizationNodeRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.mapper.OrganizationNodeWithCredentialsDTOMapper;
import org.redflag.service.util.LtreePathUtil;

import java.util.Objects;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class TransactionCreateOrganizationNodeService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeWithCredentialsDTOMapper organizationNodeWithCredentialsDTOMapper;

    @Transactional
    public OrganizationNodeWithCredentialsDTO creatNode(CreateOrganizationNodeRequest request){
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        OrganizationNode organizationNode = new OrganizationNode()
                .setName(request.getName())
                .setUuid(UUID.randomUUID())
                .setOrganization(organization)
                .setIsService(request.getIsService());
        organizationNodeRepository.save(organizationNode);
        if (Objects.isNull(request.getParentId())) {
            organizationNode.setPath(organizationNode.getId().toString());
        } else {
            OrganizationNode parentNode = organizationNodeRepository.findById(request.getParentId())
                    .orElseThrow(ErrorCatalog.NO_DATA::getException);

            organizationNode.setPath(LtreePathUtil.getChildPath(parentNode.getPath(), organizationNode.getId()));
        }
        organizationNodeRepository.update(organizationNode);

        return organizationNodeWithCredentialsDTOMapper.toOrganizationNodeWithCredentialsDTO(organizationNode);
    }

    @Transactional
    public void compensateCreateNode(Long nodeId){
        if (organizationNodeRepository.existsById(nodeId)){
            organizationNodeRepository.deleteById(nodeId);
        }
    }
}
