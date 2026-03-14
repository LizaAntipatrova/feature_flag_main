package org.redflag.dto.complex;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Introspected
@Serdeable
public class CreateOrganizationWithRootNodeResponse {

    @JsonProperty("id")
    @Schema(description = "Идентификатор созданной записи в БД", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Long id;

    @JsonProperty("name")
    @Schema(description = "Название организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "ООО Хихи-хаха")
    private final String name;

    @JsonProperty("organizationNode")
    @Schema(description = "Созданное корневое звено организации", requiredMode = Schema.RequiredMode.REQUIRED)
    private final OrganizationNodeDTO organizationNode;


    @Data
    @Introspected
    @Serdeable
    public static class OrganizationNodeDTO {

        @JsonProperty("id")
        @Schema(description = "Идентификатор записи в БД", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private final Long id;

        @JsonProperty("organizationId")
        @Schema(description = "Идентификатор организации, к которой принадлежит звено, в БД", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private final Long organizationId;

        @JsonProperty("uuid")
        @Schema(description = "Не технический идентификатор звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "9c2c7a6d-29e9-4c8c-a0b3-3b14f7c2b4f1")
        private final UUID uuid;

        @JsonProperty("path")
        @Schema(description = "Путь из идентификаторов от корня организации до текущего звена", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private final String path;

        @JsonProperty("name")
        @Schema(description = "Название звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "ООО Хихи-хаха")
        private final String name;

        @JsonProperty("isService")
        @Schema(description = "Является ли данное звено организации сервисом", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
        private final Boolean isService;

        @JsonProperty("version")
        @Schema(description = "Версия данных для оптимистичной блокировки", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private final Long version;
    }


}
