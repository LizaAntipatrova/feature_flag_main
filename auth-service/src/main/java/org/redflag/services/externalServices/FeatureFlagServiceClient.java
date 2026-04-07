package org.redflag.services.externalServices;

import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Singleton;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redflag.dto.CreateOrganizationRequest;
import org.redflag.dto.CreateOrganizationResponse;
import org.redflag.dto.OrganizationNodeDTO;
import org.redflag.exception.AccessDeniedCustomException;
import org.redflag.exception.BadCredentialsCustomException;
import org.redflag.exception.ConflictCustomException;
import org.redflag.exception.ResourceNotFoundCustomException;
import org.redflag.exception.handler.CustomErrorResponse;

import java.util.UUID;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagServiceClient {

    private final FFServiceClient ffServiceClient;

    public CreateOrganizationResponse createOrganization(String name) {
        CreateOrganizationRequest request = new CreateOrganizationRequest(name);

        try {
            return ffServiceClient.createOrganization(request);

        } catch (HttpClientResponseException e) {
            var errorBody = e.getResponse().getBody(CustomErrorResponse.class);
            String message = errorBody.map(CustomErrorResponse::getMessage)
                    .orElse("Ошибка внешнего сервиса");

            log.error("FF-Service вернул ошибку [{}]: {}", e.getStatus(), message);

            throw switch (e.getStatus()) {
                case CONFLICT -> new ConflictCustomException(message);          // 409
                case BAD_REQUEST -> new BadCredentialsCustomException(message); // 400
                case UNAUTHORIZED -> new AccessDeniedCustomException(message);  // 401
                default -> new RuntimeException("Непредвиденная ошибка: " + message);

            };
        } catch (Exception e) {
            log.error("Критическая ошибка при обращении к FF-Service: {}", e.getMessage());
            throw new RuntimeException("Сервис временно недоступен");
        }
    };

    public OrganizationNodeDTO getOrganizationNodeByUuid(UUID organizationNodeUuid) {
        try {
            return ffServiceClient.getOrganizationNodeByUuid(organizationNodeUuid);

        } catch (HttpClientResponseException e) {
            var errorBody = e.getResponse().getBody(CustomErrorResponse.class);
            String message = errorBody.map(CustomErrorResponse::getMessage)
                    .orElse("Ошибка main service");

            log.error("Main-Service вернул ошибку [{}]: {}", e.getStatus(), message);

            throw switch (e.getStatus()) {
                case UNAUTHORIZED -> new AccessDeniedCustomException(message);          // 401
                case FORBIDDEN -> new AccessDeniedCustomException(message);              // 403
                case NOT_FOUND -> new ResourceNotFoundCustomException(message);          // 404
                case BAD_REQUEST -> new BadCredentialsCustomException(message);          // 400
                default -> new RuntimeException("Непредвиденная ошибка: " + message);
            };

        } catch (Exception e) {
            log.error("Критическая ошибка при обращении к Main-Service: {}", e.getMessage(), e);
            throw new RuntimeException("Сервис временно недоступен");
        }
    }

}