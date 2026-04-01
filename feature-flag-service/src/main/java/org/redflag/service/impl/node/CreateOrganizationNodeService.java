package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.AuthenticationProvider;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.create.CreateOrganizationNodeRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;
import org.redflag.service.util.LtreePathUtil;

import java.util.Objects;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class CreateOrganizationNodeService extends BaseService<CreateOrganizationNodeRequest, OrganizationNodeDTO> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void validateRequest(CreateOrganizationNodeRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("name");
        }
        if (Objects.isNull(request.getIsService())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("isService");
        }
    }

    @Override
    protected void validateState(CreateOrganizationNodeRequest request) {
        UUID authNode = authenticationProvider.getAuthenticationNodeUuid();
        if (Objects.isNull(request.getParentId())){
            if (!organizationNodeRepository.isRootNodeInOrganization(authNode, request.getOrganizationId())) {
                throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
            }
        }else{
            if (!organizationNodeRepository.existsChildNodeInParentNodeByChildIdAndParentUuid(
                    request.getParentId(), authNode)) {
                throw ErrorCatalog.NO_RIGHTS_TO_ENTITY.getException();
            }
            if (!organizationNodeRepository.isNodeInOrganization(request.getParentId(), request.getOrganizationId())){
                throw ErrorCatalog.NO_SUCH_NODE_IN_ORGANIZATION.getException();
            }
        }
        if (organizationNodeRepository.existsByOrganization_IdAndName(request.getOrganizationId(), request.getName())) {
            throw ErrorCatalog.NOT_UNIQUE_ORGANIZATION_NODE_NAME_IN_ORGANIZATION.getException();
        }
        if (Objects.isNull(request.getParentId())
                && organizationNodeRepository.existsRootNodeInOrganization(request.getOrganizationId())) {
            throw ErrorCatalog.ORGANIZATION_CAN_HAVE_ONE_ROOT_NODE.getException();
        }

        if (Objects.nonNull(request.getParentId())) {
            OrganizationNode parentNode = organizationNodeRepository.findById(request.getParentId())
                    .orElseThrow(ErrorCatalog.NO_DATA::getException);
            if (parentNode.getIsService()) {
                throw ErrorCatalog.SERVICE_CANNOT_HAVE_DESCENDANTS.getException();
            }
            if (!parentNode.getOrganization().getId().equals(request.getOrganizationId())) {
                throw ErrorCatalog.PARENT_NODE_MUST_BE_IN_SAME_ORGANIZATION.getException();
            }
        }
    }

    @Override
    protected OrganizationNodeDTO execute(CreateOrganizationNodeRequest request) {
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

        return organizationNodeDTOMapper.toOrganizationNodeDTO(organizationNode);
    }


}
