package org.redflag.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.ErrorResponse;
import org.redflag.dto.organization.create.CreateOrganizationRequest;
import org.redflag.dto.organization.create.CreateOrganizationResponse;
import org.redflag.dto.organization.delete.DeleteOrganizationRequest;
import org.redflag.dto.organization.get.GetOrganizationByIdRequest;
import org.redflag.dto.organization.get.GetOrganizationByIdResponse;
import org.redflag.dto.organization.get.GetOrganizationsRequest;
import org.redflag.dto.organization.get.GetOrganizationsResponse;
import org.redflag.dto.organization.update.UpdateOrganizationRequest;
import org.redflag.dto.organization.update.UpdateOrganizationResponse;
import org.redflag.service.impl.*;

@Controller("api/v1/organizations")
@RequiredArgsConstructor
@Tag(name = "Организация")
public class OrganizationController {
    private final CreateOrganizationService createOrganizationService;
    private final GetOrganizationsService getOrganizationsService;
    private final GetOrganizationByIdService getOrganizationByIdService;
    private final UpdateOrganizationService updateOrganizationService;
    private final DeleteOrganizationService deleteOrganizationService;

    @Post
    @Operation(
            summary = "Создать организацию",
            description = "Cоздает организацию, если нет организации с таким же именем"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешный ответ создания",
                    content = @Content(schema = @Schema(implementation = CreateOrganizationResponse.class))
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
    public HttpResponse<CreateOrganizationResponse> createOrganization(@Body CreateOrganizationRequest request) {
        return HttpResponse.created(createOrganizationService.service(request));
    }

    @Get
    @Operation(
            summary = "Получить список организаций",
            description = "Возвращает список организаций с пагинацией"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetOrganizationsResponse.class))
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

    public GetOrganizationsResponse getOrganizations(
            @Parameter(description = "Верхний лимит количества записей для получения блока записей", required = true, example = "42")
            @QueryValue("limit") Integer limit,
            @Parameter(description = "Начальный номер записи от начала для получения блока записей", required = true, example = "0")
            @QueryValue("offset") Integer offset) {
        GetOrganizationsRequest request = new GetOrganizationsRequest(limit, offset);
        return getOrganizationsService.service(request);
    }

    @Get("/{organizationId}")
    @Operation(
            summary = "Получить организацию",
            description = "Получить конкретную организацию по id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetOrganizationByIdResponse.class))
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
    public GetOrganizationByIdResponse getOrganizationById(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId
    ) {
        GetOrganizationByIdRequest request = new GetOrganizationByIdRequest(organizationId);
        return getOrganizationByIdService.service(request);
    }

    @Patch("/{organizationId}")
    @Operation(
            summary = "Обновить информацию об организации",
            description = "Обновить информацию о конкретной организации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = UpdateOrganizationResponse.class))
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
    public UpdateOrganizationResponse updateOrganization(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Body UpdateOrganizationRequest updateOrganizationRequest
    ) {
        updateOrganizationRequest.setId(organizationId);
        return updateOrganizationService.service(updateOrganizationRequest);
    }

    @Delete("/{organizationId}")
    @Operation(
            summary = "Удалить организацию",
            description = "Удалить конкретную организации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Успешный ответ удаления"
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
    public HttpResponse<Void> deleteOrganization(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId
    ) {
        DeleteOrganizationRequest request = new DeleteOrganizationRequest(organizationId);
        deleteOrganizationService.service(request);
        return HttpResponse.noContent();
    }

}
