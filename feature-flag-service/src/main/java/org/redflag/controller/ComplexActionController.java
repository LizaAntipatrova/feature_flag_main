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
import org.redflag.dto.ErrorResponse;
import org.redflag.dto.complex.CreateOrganizationWithRootNodeRequest;
import org.redflag.dto.complex.CreateOrganizationWithRootNodeResponse;

import java.util.UUID;

@Controller("/api/vi")
@Tag(name = "Действия с несколькими сущностями")
public class ComplexActionController {

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
    public HttpResponse<CreateOrganizationWithRootNodeResponse> createOrganization(@Body CreateOrganizationWithRootNodeRequest request) {
        return HttpResponse.created(new CreateOrganizationWithRootNodeResponse(1L, "Хехе",
                new CreateOrganizationWithRootNodeResponse.OrganizationNodeDTO(1L,
                        1L,
                        UUID.fromString("9c2c7a6d-29e9-4c8c-a0b3-3b14f7c2b4f1"),
                        "1",
                        "Хехе}",
                        false,
                        1L)));
    }
}
