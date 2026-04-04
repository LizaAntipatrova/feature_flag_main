package org.redflag.service.impl.node;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
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
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;
import org.redflag.service.validator.OrganizationNodeValidator;
import org.redflag.service.validator.UniqueNameValidator;

import java.util.Objects;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class CreateOrganizationNodeService extends BaseService<CreateOrganizationNodeRequest, OrganizationNodeDTO> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;
    private final OrganizationNodeValidator organizationNodeValidator;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;
    private final UniqueNameValidator uniqueNameValidator;

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
        Boolean isNewRootNode = Objects.isNull(request.getParentId());
        if (isNewRootNode) {
            authRightsToNodeValidator.checkIsAuthNodeInOrganization(request.getOrganizationId());
            organizationNodeValidator.checkMissingRootNodeInOrganization(request.getOrganizationId());
        } else {
            authRightsToNodeValidator.checkIsAuthNodeIsParentToRequestNode(request.getParentId());
            linkedEntityValidator.checkIsNodeInOrganization(request.getParentId(), request.getOrganizationId());
            organizationNodeValidator.checkNodeIsNotService(request.getParentId());
        }
        uniqueNameValidator.checkIsNodeNameMissingInOrganization(request.getOrganizationId(), request.getName());
    }


    @Override
    @Transactional
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
