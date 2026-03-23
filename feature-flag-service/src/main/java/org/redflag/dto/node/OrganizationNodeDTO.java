package org.redflag.dto.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
@RequiredArgsConstructor
@Introspected
@Serdeable
public class OrganizationNodeDTO {

    @JsonProperty("id")
    @Schema(description = "Идентификатор записи в БД", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    protected final Long id;

    @JsonProperty("organizationId")
    @Schema(description = "Идентификатор организации, к которой принадлежит звено, в БД", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    protected final Long organizationId;

    @JsonProperty("uuid")
    @Schema(description = "Не технический идентификатор звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "9c2c7a6d-29e9-4c8c-a0b3-3b14f7c2b4f1")
    protected final UUID uuid;

    @JsonProperty("path")
    @Schema(description = "Путь из идентификаторов от корня организации до текущего звена", requiredMode = Schema.RequiredMode.REQUIRED, example = "100.1")
    protected final String path;

    @JsonProperty("name")
    @Schema(description = "Название звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "Кредитование")
    protected final String name;

    @JsonProperty("isService")
    @Schema(description = "Является ли данное звено организации сервисом", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    protected final Boolean isService;

    @JsonProperty("version")
    @Schema(description = "Версия данных для оптимистичной блокировки", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    protected final Long version;
}