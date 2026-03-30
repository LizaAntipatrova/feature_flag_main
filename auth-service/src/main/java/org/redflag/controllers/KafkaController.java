package org.redflag.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redflag.dto.CreateServiceAccessResponse;
import org.redflag.dto.SdkLoginRequest;
import org.redflag.services.kafkaServices.KafkaGeneralService;

@Slf4j
@Controller("/api/v1/kafka")
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class KafkaController {

    private final KafkaGeneralService kafkaGeneralService;

    /**
     * Создает полную инфраструктуру для нового сервиса:
     * Топик + Пользователь + ACL
     */
    @Post("/provision")
    public HttpResponse<CreateServiceAccessResponse> provision(@Body SdkLoginRequest request) throws Exception {
        log.info("Received provision request for serviceId: {}", request.newLogin());

        CreateServiceAccessResponse response = kafkaGeneralService.setupKafka(
                request.newLogin().toString(),
                "password"
        );

        return HttpResponse.created(response);
    }

    /**
     * Удаляет всю инфраструктуру для сервиса.
     * Использует DELETE, принимает ID сервиса и пароль в теле запроса.
     */
    @Delete("/deprovision")
    public HttpResponse<?> deprovision(@Body SdkLoginRequest request) throws Exception {
        log.info("Received deprovision request for serviceId: {}", request.newLogin());

        kafkaGeneralService.cleanupKafka(
                String.valueOf(request.newLogin()),
                "password"
        );

        return HttpResponse.noContent();
    }

}