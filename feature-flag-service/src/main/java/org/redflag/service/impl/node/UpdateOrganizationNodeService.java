package org.redflag.service.impl.node;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.redflag.client.AuthClient;
import org.redflag.dto.auth.CreateServiceCredentialsResponse;
import org.redflag.dto.auth.LoginForServiceCredentialsDto;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.node.OrganizationNodeWithCredentialsDTO;
import org.redflag.dto.node.update.UpdateOrganizationNodeRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.model.OrganizationNode;
import org.redflag.repository.OrganizationNodeRepository;
import org.redflag.service.BaseService;
import org.redflag.service.client.AuthClientService;
import org.redflag.service.mapper.OrganizationNodeDTOMapper;
import org.redflag.service.transaction.TransactionUpdateOrganizationNodeService;
import org.redflag.service.transaction.UpdateOrganizationNodeTransactionalResult;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.EntityVersionValidator;
import org.redflag.service.validator.LinkedEntityValidator;
import org.redflag.service.validator.UniqueNameValidator;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class UpdateOrganizationNodeService extends BaseService<UpdateOrganizationNodeRequest, OrganizationNodeDTO> {
    private final OrganizationNodeRepository organizationNodeRepository;
    private final OrganizationNodeDTOMapper organizationNodeDTOMapper;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;
    private final UniqueNameValidator uniqueNameValidator;
    private final EntityVersionValidator entityVersionValidator;
    private final TransactionUpdateOrganizationNodeService transactionUpdateOrganizationNodeService;
    private final AuthClientService authClientService;


    @Override
    protected void validateRequest(UpdateOrganizationNodeRequest request) {
        String name = request.getName();
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("name");
        }
        if (Objects.isNull(request.getIsService())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("isService");
        }
        if (Objects.isNull(request.getVersion())) {
            throw ErrorCatalog.EMPTY_FIELD.withMessageArgs("version");
        }
    }

    @Override
    protected void validateState(UpdateOrganizationNodeRequest request) {
        authRightsToNodeValidator.checkIsAuthNodeIsParentToRequestNode(request.getNodeId());
        linkedEntityValidator.checkIsNodeInOrganization(request.getNodeId(), request.getOrganizationId());

        OrganizationNode organizationNode = organizationNodeRepository
                .findByOrganization_IdAndId(request.getOrganizationId(), request.getNodeId())
                .orElseThrow(ErrorCatalog.NO_DATA::getException);
        if (!organizationNode.getName().equals(request.getName())) {
            uniqueNameValidator.checkIsNodeNameMissingInOrganization(request.getOrganizationId(), request.getName());
        }
        if (request.getIsService() && organizationNodeRepository.existsDescendants(request.getNodeId())) {
            throw ErrorCatalog.SERVICE_CANNOT_HAVE_DESCENDANTS.getException();
        }

    }

    @Override
    @Transactional
    protected OrganizationNodeWithCredentialsDTO execute(UpdateOrganizationNodeRequest request) {
        UpdateOrganizationNodeTransactionalResult transactionalResult =
                transactionUpdateOrganizationNodeService.updateNode(request);
        OrganizationNodeWithCredentialsDTO resultDto = transactionalResult.getDto();
        try {
            Boolean newIsService = resultDto.getIsService();
            if (!transactionalResult.getOldIsService().equals(newIsService)){
                LoginForServiceCredentialsDto loginDto = LoginForServiceCredentialsDto.builder()
                        .newLogin(resultDto.getUuid())
                        .build();
                if(newIsService){
                    CreateServiceCredentialsResponse credentialsResponse =
                            authClientService.createServiceCredentials(request.getSessionCookie(),loginDto);
                    resultDto.setUsername(credentialsResponse.getUsername())
                            .setPassword(credentialsResponse.getPassword())
                            .setTopicName(credentialsResponse.getTopic())
                            .setGroupName(credentialsResponse.getGroupName());
                }else {
                    authClientService.deleteServiceCredentials(
                            request.getSessionCookie(),
                            loginDto);
                }
            }
            return resultDto;
        }catch (Exception e){
            transactionUpdateOrganizationNodeService.compensateUpdateNode(transactionalResult);
            throw ErrorCatalog.AUTH_SERVICE_ERROR.getException();
        }

    }
}
