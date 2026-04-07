package org.redflag.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redflag.auth.Role;
import org.redflag.dto.ErrorResponse;
import org.redflag.dto.complex.CreateOrganizationWithRootNodeResponse;
import org.redflag.dto.complex.GetFeatureFlagsByUuidServiceNodeResponse;
import org.redflag.dto.complex.OrganizationNodeUuidDTO;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.organization.create.CreateOrganizationRequest;
import org.redflag.service.impl.CreateOrganizationWithRootNodesService;
import org.redflag.service.impl.GetFeatureFlagsByUuidServiceNodeService;
import org.redflag.service.impl.GetOrganizationNodeByUuidService;

import java.util.UUID;

@Controller("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Доп ручки")
//Пока хз как эти ручки классифицировать
public class ChepuhaController {

    private final CreateOrganizationWithRootNodesService createOrganizationWithRootNodesService;
    private final GetOrganizationNodeByUuidService getOrganizationNodeByUuidService;
    private final GetFeatureFlagsByUuidServiceNodeService getFeatureFlagsByUuidServiceNodeService;


    @Post("/organizations/with-root-node")
    @Operation(
            summary = "Создать организацию",
            description = "Cоздает организацию вместе с корневым звеном организации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешный ответ создания",
                    content = @Content(schema = @Schema(implementation = CreateOrganizationWithRootNodeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Запрос без авторизации",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав для выполнения этого действия",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Конфликт данных",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )

    })
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<CreateOrganizationWithRootNodeResponse> createOrganizationWithRootNode(@Body CreateOrganizationRequest request) {
        return HttpResponse.created(createOrganizationWithRootNodesService.service(request));
    }

    @Get("/find-node")
    @Operation(
            summary = "Получить звено организации по uuid",
            description = "Получает звено организации по uuid",
            security = @SecurityRequirement(name = "sessionAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = OrganizationNodeDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Запрос без авторизации",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав для выполнения этого действия",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )

    })
    @Secured({Role.READ_DEPARTMENT_ROLE_NAME, Role.ROLE_SDK_CLIENT_NAME})
    public OrganizationNodeDTO getOrganizationNodeByUuid(
            @Parameter(description = "UUID звена организации", required = true, example = "9c2c7a6d-29e9-4c8c-a0b3-3b14f7c2b4f1")
            @QueryValue UUID organizationNodeUuid) {
        OrganizationNodeUuidDTO request = OrganizationNodeUuidDTO.builder()
                .nodeUuid(organizationNodeUuid)
                .build();
        return getOrganizationNodeByUuidService.service(request);
    }

    @Get("/find-flags")
    @Operation(
            summary = "Получить все фича-флаги сервиса",
            description = "Получает все фича флаги сервиса и его предков по uuid сервиса"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetFeatureFlagsByUuidServiceNodeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Запрос без авторизации",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Недостаточно прав для выполнения этого действия",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найдено",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )

    })
    @Secured(Role.ROLE_SDK_CLIENT_NAME)
    public GetFeatureFlagsByUuidServiceNodeResponse getFeatureFlagsByUuidServiceNode(
            @Parameter(description = "UUID сервисной ноды организации", required = true, example = "9c2c7a6d-29e9-4c8c-a0b3-3b14f7c2b4f1")
            @QueryValue UUID organizationNodeUuid) {
        OrganizationNodeUuidDTO request = OrganizationNodeUuidDTO.builder()
                .nodeUuid(organizationNodeUuid)
                .build();
        return getFeatureFlagsByUuidServiceNodeService.service(request);
    }
}
