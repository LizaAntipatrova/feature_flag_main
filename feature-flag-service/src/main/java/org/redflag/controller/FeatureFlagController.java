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
import org.redflag.dto.featureflag.create.CreateFeatureFlagRequest;
import org.redflag.dto.featureflag.create.CreateFeatureFlagResponse;
import org.redflag.dto.featureflag.delete.DeleteFeatureFlagRequest;
import org.redflag.dto.featureflag.get.*;
import org.redflag.dto.featureflag.update.UpdateFeatureFlagRequest;
import org.redflag.dto.featureflag.update.UpdateFeatureFlagResponse;
import org.redflag.dto.featureflag.get.GetFeatureFlagByIdRequest;
import org.redflag.service.impl.*;

@RequiredArgsConstructor
@Controller("api/v1/organizations/{organizationId}/nodes/{nodeId}/feature-flags")
@Tag(name = "Фича флаг")
public class FeatureFlagController {
    private final CreateFeatureFlagService createFeatureFlagService;
    private final GetFeatureFlagByIdService getFeatureFlagByIdService;
    private final UpdateFeatureFlagService updateFeatureFlagService;
    private final DeleteFeatureFlagService deleteFeatureFlagService;
    private final GetFeatureFlagByNameService getFeatureFlagByNameService;
    private final GetFeatureFlagsService getFeatureFlagsService;
    private final GetLinkedFeatureFlagsService getLinkedFeatureFlagsService;

    @Post
    @Operation(
            summary = "Создать фича флаг",
            description = "Cоздает фича флаг в звене организации, если нет фича флага с таким же именем в этой организации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешный ответ создания",
                    content = @Content(schema = @Schema(implementation = CreateFeatureFlagResponse.class))
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
    public HttpResponse<CreateFeatureFlagResponse> createOrganizationNode(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId,
            @Body CreateFeatureFlagRequest request) {
        request.setOrganizationId(organizationId);
        request.setNodeId(nodeId);
        return HttpResponse.created(createFeatureFlagService.service(request));
    }

    @Get
    @Operation(
            summary = "Получить фича флаги звена организации",
            description = "Получает фича флаги только указанного звена организации с пагинацией"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetFeatureFlagsResponse.class))
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
    public GetFeatureFlagsResponse getFeatureFlags(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId,
            @Parameter(description = "Верхний лимит количества записей для получения блока записей", required = true, example = "42")
            @QueryValue("limit") Integer limit,
            @Parameter(description = "Начальный номер записи от начала для получения блока записей", required = true, example = "0")
            @QueryValue("offset") Integer offset) {
        GetFeatureFlagsRequest request = new GetFeatureFlagsRequest(organizationId, nodeId, limit, offset);
        return getFeatureFlagsService.service(request);
    }

    @Get("/linked")
    @Operation(
            summary = "Получить фича флаги связанных звеньев организации",
            description = "Получает с пагинацией фича флаги звеньев организации связанные с конкретным звеном отношениями: само звено, предок, потомок "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetLinkedFeatureFlagsResponse.class))
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
    public GetLinkedFeatureFlagsResponse getLinkedFeatureFlags(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId,
            @Parameter(description = "Тип отношения", required = true, example = "self")
            @QueryValue("relation") RelationType relation,
            @Parameter(description = "Верхний лимит количества записей для получения блока записей", required = true, example = "42")
            @QueryValue("limit") Integer limit,
            @Parameter(description = "Начальный номер записи от начала для получения блока записей", required = true, example = "0")
            @QueryValue("offset") Integer offset) {


        GetLinkedFeatureFlagsRequest request = new GetLinkedFeatureFlagsRequest(organizationId, nodeId, relation, limit, offset);
        return getLinkedFeatureFlagsService.service(request);
    }

    @Get("/find")
    @Operation(
            summary = "Получить фича флаг по имени",
            description = "Получить фича флаг по имени"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetFeatureFlagByNameResponse.class))
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
    public GetFeatureFlagByNameResponse getFeatureFlagByName(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId,
            @Parameter(description = "Название фича флага", required = true, example = "meow_mode")
            @QueryValue String flagName
    ) {
        GetFeatureFlagByNameRequest request = new GetFeatureFlagByNameRequest(organizationId, nodeId, flagName);
        return getFeatureFlagByNameService.service(request);
    }

    @Get("/{flagId}")
    @Operation(
            summary = "Получить фича флаг",
            description = "Получить фича флаг по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetFeatureFlagByIdResponse.class))
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
    public GetFeatureFlagByIdResponse getFeatureFlagById(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId,
            @Parameter(description = "Идентификатор фича флага", required = true, example = "1")
            @PathVariable Long flagId
    ) {
        GetFeatureFlagByIdRequest request = new GetFeatureFlagByIdRequest(organizationId, nodeId, flagId);
        return getFeatureFlagByIdService.service(request) ;
    }

    @Patch("/{flagId}")
    @Operation(
            summary = "Изменить информацию о фича флаге",
            description = "Изменение информации о конкретном фича флаге"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = UpdateFeatureFlagResponse.class))
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
    public UpdateFeatureFlagResponse updateFeatureFlag(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId,
            @Parameter(description = "Идентификатор фича флага", required = true, example = "1")
            @PathVariable Long flagId,
            @Body UpdateFeatureFlagRequest updateFeatureFlagRequest
    ) {
        updateFeatureFlagRequest.setOrganizationId(organizationId);
        updateFeatureFlagRequest.setNodeId(nodeId);
        updateFeatureFlagRequest.setFeatureFlagId(flagId);
        return updateFeatureFlagService.service(updateFeatureFlagRequest) ;
    }

    @Delete("/{flagId}")
    @Operation(
            summary = "Удалить фича флаг",
            description = "Удалить конкретный фича флаг"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Успешный ответ"
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
    public HttpResponse<Void> deleteFeatureFlag(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId,
            @Parameter(description = "Идентификатор фича флага", required = true, example = "1")
            @PathVariable Long flagId
    ) {
        DeleteFeatureFlagRequest request = new DeleteFeatureFlagRequest(organizationId, nodeId, flagId);
        deleteFeatureFlagService.service(request);
        return HttpResponse.noContent();
    }


}
