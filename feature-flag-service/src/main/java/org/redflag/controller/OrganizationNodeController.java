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
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.ErrorResponse;
import org.redflag.dto.node.create.CreateOrganizationNodeRequest;
import org.redflag.dto.node.create.CreateOrganizationNodeResponse;
import org.redflag.dto.node.delete.DeleteOrganizationNodeRequest;
import org.redflag.dto.node.get.*;
import org.redflag.dto.node.update.MoveOrganizationNodeRequest;
import org.redflag.dto.node.update.MoveOrganizationNodeResponse;
import org.redflag.dto.node.update.UpdateOrganizationNodeRequest;
import org.redflag.dto.node.update.UpdateOrganizationNodeResponse;
import org.redflag.service.impl.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Controller("api/v1/organizations/{organizationId}/nodes")
@Tag(name = "Звено организации")
public class OrganizationNodeController {
    private final CreateOrganizationNodeService createOrganizationNodeService;
    private final GetOrganizationNodeByIdService getOrganizationNodeByIdService;
    private final UpdateOrganizationNodeService updateOrganizationNodeService;
    private final DeleteOrganizationNodeService deleteOrganizationNodeService;
    private final GetOrganizationNodesService getOrganizationNodesService;
    private final GetAncestorsOrganizationNodesService getAncestorsOrganizationNodesService;
    private final GetChildrenOrganizationNodesService getChildrenOrganizationNodesService;
    private final GetDescendantsOrganizationNodesService getDescendantsOrganizationNodesService;
    private final GetSubtreeNodeOrganizationsService getSubtreeNodeOrganizationsService;


    @Post
    @Operation(
            summary = "Создать звено организации",
            description = "Cоздает звено организации, если нет звена с таким же именем в этой организации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешный ответ создания",
                    content = @Content(schema = @Schema(implementation = CreateOrganizationNodeResponse.class))
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
    public HttpResponse<CreateOrganizationNodeResponse> createOrganizationNode(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Body CreateOrganizationNodeRequest request) {
        request.setOrganizationId(organizationId);
        return HttpResponse.created(createOrganizationNodeService.service(request));
    }

    @Get("/{nodeId}")
    @Operation(
            summary = "Получить звено организации",
            description = "Получает звено организации по id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetOrganizationNodeByIdResponse.class))
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
    public GetOrganizationNodeByIdResponse getOrganizationNodeById(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId) {
        GetOrganizationNodeByIdRequest getOrganizationNodeByIdRequest = new GetOrganizationNodeByIdRequest(organizationId, nodeId);
        return getOrganizationNodeByIdService.service(getOrganizationNodeByIdRequest);
    }

    @Get()
    @Operation(
            summary = "Получить список звеньев организации",
            description = "Получает звенья организации с пагинацией и возможностью фильтровать по родительскому звену "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetOrganizationNodesResponse.class))
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
    public GetOrganizationNodesResponse getOrganizationNodes(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Nullable
            @Parameter(description = "Идентификатор родительского звена организации", required = false, example = "100")
            @QueryValue Long parentId,
            @Parameter(description = "Верхний лимит количества записей для получения блока записей", required = true, example = "42")
            @QueryValue("limit") Integer limit,
            @Parameter(description = "Начальный номер записи от начала для получения блока записей", required = true, example = "0")
            @QueryValue("offset") Integer offset
    ) {
        GetOrganizationNodesRequest getOrganizationNodesRequest = new GetOrganizationNodesRequest(organizationId, parentId, limit, offset);
        return getOrganizationNodesService.service(getOrganizationNodesRequest);
    }

    @Patch("/{nodeId}")
    @Operation(
            summary = "Обновить информацию о звене организации",
            description = "Обновляет информацию у конкретного звена организации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = UpdateOrganizationNodeResponse.class))
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
    public UpdateOrganizationNodeResponse updateOrganizationNode(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId,
            @Body UpdateOrganizationNodeRequest updateOrganizationNodeRequest
    ) {
        updateOrganizationNodeRequest.setOrganizationId(organizationId);
        updateOrganizationNodeRequest.setNodeId(nodeId);
        return updateOrganizationNodeService.service(updateOrganizationNodeRequest);
    }

    @Delete("/{nodeId}")
    @Operation(
            summary = "Удалить звено организации",
            description = "Удаляет конкретное звено организации со всеми вложенными звеньями"
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
    public HttpResponse<Void> deleteOrganizationNode(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId
    ) {
        DeleteOrganizationNodeRequest request = new DeleteOrganizationNodeRequest(organizationId, nodeId);
        deleteOrganizationNodeService.service(request);
        return HttpResponse.noContent();
    }

    @Get("/{nodeId}/children")
    @Operation(
            summary = "Получить список дочерних звеньев организации",
            description = "Получает непосредственных детей звена организации "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetChildrenOrganizationNodesResponse.class))
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
    public GetChildrenOrganizationNodesResponse getChildrenOrganizationNodes(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId
    ) {
        GetChildrenOrganizationNodesRequest request = new GetChildrenOrganizationNodesRequest(organizationId, nodeId);
        return getChildrenOrganizationNodesService.service(request);
    }

    @Get("/{nodeId}/ancestors")
    @Operation(
            summary = "Получить список родительских звеньев организации",
            description = "Получает всех предков конкретного звена организации, включая само звено"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetAncestorsOrganizationNodesResponse.class))
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
    public GetAncestorsOrganizationNodesResponse getAncestorsOrganizationNodes(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId
    ) {
        GetAncestorsOrganizationNodesRequest request = new GetAncestorsOrganizationNodesRequest(organizationId, nodeId);
        return getAncestorsOrganizationNodesService.service(request);
    }

    @Get("/{nodeId}/descendants")
    @Operation(
            summary = "Получить потомков звена организации",
            description = "Получает всех потомков звена организации, с учетом максимально заданного уровня потомков"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetDescendantsOrganizationNodesResponse.class))
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
    public GetDescendantsOrganizationNodesResponse getDescendantsOrganizationNodes(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId,
            @Nullable
            @Parameter(description = "Максимальный уровень потомка, от текущего звена (по умолчанию возвращает всех потомков)", required = false, example = "1")
            @QueryValue("depth") Integer depth
    ) {
        GetDescendantsOrganizationNodesRequest request = new GetDescendantsOrganizationNodesRequest(organizationId, nodeId, depth);
        return getDescendantsOrganizationNodesService.service(request);
    }

    @Get("/{nodeId}/subtree")
    @Operation(
            summary = "Получить поддерево иерархии звеньев организации",
            description = "Получает поддерево иерархии звеньев организации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = GetSubtreeNodeOrganizationsResponse.class))
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
    public GetSubtreeNodeOrganizationsResponse getSubtreeOrganizationNodes(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "1")
            @PathVariable Long nodeId
    ) {
        GetSubtreeNodeOrganizationsRequest request = new GetSubtreeNodeOrganizationsRequest(organizationId, nodeId);
        return getSubtreeNodeOrganizationsService.service(request);
    }

    @Post("/{nodeId}/move")
    @Operation(
            summary = "Переместить звено организации в иерархии",
            description = "Перемещает поддерево звена организации к другому родителю в иерархии звеньев организации"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = MoveOrganizationNodeResponse.class))
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
    public MoveOrganizationNodeResponse moveOrganizationNode(
            @Parameter(description = "Идентификатор организации", required = true, example = "1")
            @PathVariable Long organizationId,
            @Parameter(description = "Идентификатор звена организации", required = true, example = "100")
            @PathVariable Long nodeId,
            @Body MoveOrganizationNodeRequest moveOrganizationNodeRequest
    ) {
        return new MoveOrganizationNodeResponse(
                nodeId,
                UUID.fromString("9c2c7a6d-29e9-4c8c-a0b3-3b14f7c2b4f1"),
                "1.100",
                "1.3.100",
                moveOrganizationNodeRequest.getVersion() + 1,
                List.of(new MoveOrganizationNodeResponse(
                        103L,
                        UUID.fromString("c3b7aef2-30d5-4dfe-9e8b-8f3c6b16a4fb"),
                        "1.100.103",
                        "1.3.100.103",
                        3L,
                        Collections.emptyList())));
    }

}
