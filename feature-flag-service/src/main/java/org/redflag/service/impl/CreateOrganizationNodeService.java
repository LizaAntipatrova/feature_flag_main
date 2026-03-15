package org.redflag.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.node.create.CreateOrganizationNodeRequest;
import org.redflag.dto.node.create.CreateOrganizationNodeResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.error.ErrorType;
import org.redflag.model.Organization;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.repository.OrganizationRepository;
import org.redflag.service.AbstractService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class CreateOrganizationNodeService extends AbstractService<CreateOrganizationNodeRequest, CreateOrganizationNodeResponse> {
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
        if (Objects.nonNull(request.getParentId())){
            Optional<OrganizationNode> organizationNode = organizationNodeRepository.findById(request.getParentId());
            if (organizationNode.isPresent()) {
                if (organizationNode.get().getIsService()){
                    throw ErrorCatalog.SERVICE_CANNOT_HAVE_DESCENDANTS.getException();
                }
            } else {
                throw ErrorCatalog.NO_DATA.getException();
            }
        }
    }

    @Override
    protected CreateOrganizationNodeResponse logic(CreateOrganizationNodeRequest request) {
        Optional<Organization> organizationOpt = organizationRepository.findById(request.getOrganizationId());
        if(organizationOpt.isEmpty()){
            throw ErrorCatalog.NO_DATA.getException();
        }
        Organization organization = organizationOpt.get();
        OrganizationNode organizationNode = new OrganizationNode()
                .setName(request.getName())
                .setUuid(UUID.randomUUID())
                .setOrganization(organization)
                .setIsService(request.getIsService());
        organizationNodeRepository.save(organizationNode);
        if (Objects.isNull(request.getParentId())){
            organizationNode.setPath(organizationNode.getId().toString());
        }else{
            OrganizationNode parentNode = organizationNodeRepository.findById(request.getParentId()).get();
            //TODO: вынести конкатенацию для path
            organizationNode.setPath(parentNode.getPath()+ "."+ organizationNode.getId().toString());
        }
        organizationNodeRepository.update(organizationNode);

        return new CreateOrganizationNodeResponse(organizationNode.getId(),
                request.getOrganizationId(),
                organizationNode.getUuid(),
                organizationNode.getPath(),
                organizationNode.getName(),
                organizationNode.getIsService(),
                organizationNode.getVersion()
                );
    }
}
