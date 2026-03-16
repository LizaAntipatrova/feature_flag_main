package org.redflag.dto.node.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Introspected
@Serdeable
public class GetDescendantsOrganizationNodesResponse {

    @JsonProperty("nodeId")
    @Schema(description = "Идентификатор дочернего звена", requiredMode = Schema.RequiredMode.REQUIRED, example = "130")
    private final Long nodeId;


    @JsonProperty("depth")
    @Schema(description = "Максимальный уровень потомков от текущего", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private final Integer depth;


    @JsonProperty("items")
    @Schema(description = "Массив звеньев-предков", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<OrganizationNodeDTO> items;


    @Data
    @Introspected
    @Serdeable
    public static class OrganizationNodeDTO {

        @JsonProperty("id")
        @Schema(description = "Идентификатор записи в БД", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        private final Long id;

        @JsonProperty("organizationId")
        @Schema(description = "Идентификатор организации, к которой принадлежит звено, в БД", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private final Long organizationId;

        @JsonProperty("uuid")
        @Schema(description = "Не технический идентификатор звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "9c2c7a6d-29e9-4c8c-a0b3-3b14f7c2b4f1")
        private final UUID uuid;

        @JsonProperty("path")
        @Schema(description = "Путь из идентификаторов от корня организации до текущего звена", requiredMode = Schema.RequiredMode.REQUIRED, example = "100.130.155")
        private final String path;

        @JsonProperty("name")
        @Schema(description = "Название звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "Кредитование")
        private final String name;

        @JsonProperty("isService")
        @Schema(description = "Является ли данное звено организации сервисом", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
        private final Boolean isService;

        @JsonProperty("version")
        @Schema(description = "Версия данных для оптимистичной блокировки", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private final Long version;
    }
}
