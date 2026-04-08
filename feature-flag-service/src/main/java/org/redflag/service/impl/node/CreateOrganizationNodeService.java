package org.redflag.service.impl.node;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.auth.CreateServiceCredentialsResponse;
import org.redflag.dto.auth.LoginForServiceCredentialsDto;
import org.redflag.dto.node.OrganizationNodeWithCredentialsDTO;
import org.redflag.dto.node.create.CreateOrganizationNodeRequest;
import org.redflag.error.ErrorCatalog;
import org.redflag.service.BaseService;
import org.redflag.service.client.AuthClientService;
import org.redflag.service.transaction.TransactionCreateOrganizationNodeService;
import org.redflag.service.validator.AuthRightsToNodeValidator;
import org.redflag.service.validator.LinkedEntityValidator;
import org.redflag.service.validator.OrganizationNodeValidator;
import org.redflag.service.validator.UniqueNameValidator;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class CreateOrganizationNodeService extends BaseService<CreateOrganizationNodeRequest, OrganizationNodeWithCredentialsDTO> {
    private final OrganizationNodeValidator organizationNodeValidator;
    private final AuthRightsToNodeValidator authRightsToNodeValidator;
    private final LinkedEntityValidator linkedEntityValidator;
    private final UniqueNameValidator uniqueNameValidator;
    private final AuthClientService authClientService;
    private final TransactionCreateOrganizationNodeService transactionCreateOrganizationNodeService;

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
    protected OrganizationNodeWithCredentialsDTO execute(CreateOrganizationNodeRequest request) {
        OrganizationNodeWithCredentialsDTO response = transactionCreateOrganizationNodeService.creatNode(request);

        try {
            if (response.getIsService()){
                LoginForServiceCredentialsDto dto = LoginForServiceCredentialsDto.builder()
                        .newLogin(response.getUuid()).build();

                CreateServiceCredentialsResponse credentialsResponse =
                        authClientService.createServiceCredentials(dto);

                return response.setUsername(credentialsResponse.getUsername())
                        .setPassword(credentialsResponse.getPassword())
                        .setTopicName(credentialsResponse.getTopic())
                        .setGroupName(credentialsResponse.getGroupName());
            }

            return response;
        }catch (Exception e){
            transactionCreateOrganizationNodeService.compensateCreateNode(response.getId());
            throw ErrorCatalog.AUTH_SERVICE_ERROR.getException();
        }

    }


}
