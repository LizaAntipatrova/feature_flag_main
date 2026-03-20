package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.create.CreateOrganizationNodeRequest;
import org.redflag.dto.node.create.CreateOrganizationNodeResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.Organization;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.BaseService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class CreateOrganizationNodeService extends BaseService<CreateOrganizationNodeRequest, CreateOrganizationNodeResponse> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationRepository organizationRepository;

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
    protected CreateOrganizationNodeResponse execute(CreateOrganizationNodeRequest request) {
        Optional<Organization> organizationOpt = organizationRepository.findById(request.getOrganizationId());
        if (organizationOpt.isEmpty()) {
            throw ErrorCatalog.NO_DATA.getException();
        }
        Organization organization = organizationOpt.get();
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

            //TODO: вынести конкатенацию для path
            organizationNode.setPath(parentNode.getPath() + "." + organizationNode.getId().toString());
        }
        organizationNodeRepository.update(organizationNode);

        return new CreateOrganizationNodeResponse(organizationNode.getId(),
                organizationNode.getOrganization().getId(),
                organizationNode.getUuid(),
                organizationNode.getPath(),
                organizationNode.getName(),
                organizationNode.getIsService(),
                organizationNode.getVersion()
        );
    }
}
