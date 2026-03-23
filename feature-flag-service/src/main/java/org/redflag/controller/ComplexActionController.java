package org.redflag.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.ErrorResponse;
import org.redflag.dto.complex.CreateOrganizationWithRootNodeResponse;
import org.redflag.dto.node.create.CreateOrganizationNodeRequest;
import org.redflag.dto.organization.create.CreateOrganizationRequest;
import org.redflag.service.impl.CreateOrganizationWithRootNodesService;

@Controller("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Действия с несколькими сущностями")
public class ComplexActionController {

    private final CreateOrganizationWithRootNodesService createOrganizationWithRootNodesService;

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
    public HttpResponse<CreateOrganizationWithRootNodeResponse> createOrganizationWithRootNode(@Body CreateOrganizationRequest request) {
        return HttpResponse.created(createOrganizationWithRootNodesService.service(request));
    }
}
