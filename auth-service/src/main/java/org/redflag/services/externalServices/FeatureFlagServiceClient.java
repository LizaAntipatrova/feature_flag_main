package org.redflag.services.externalServices;

import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Singleton;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redflag.dto.CreateOrganizationRequest;
import org.redflag.dto.CreateOrganizationResponse;
import org.redflag.exception.AccessDeniedCustomException;
import org.redflag.exception.BadCredentialsCustomException;
import org.redflag.exception.ConflictCustomException;
import org.redflag.exception.handler.CustomErrorResponse;

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

            throw switch (e.getStatus().getCode()) {
                case 409 -> new ConflictCustomException(message);
                case 400 -> new BadCredentialsCustomException(message);
                case 401 -> new AccessDeniedCustomException(message);
                default  -> new RuntimeException("Непредвиденная ошибка: " + message);
            };
        } catch (Exception e) {
            log.error("Критическая ошибка при обращении к FF-Service: {}", e.getMessage());
            throw new RuntimeException("Сервис временно недоступен");
        }
    };

}