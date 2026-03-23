package org.redflag.dto.node.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.redflag.dto.node.OrganizationNodeDTO;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Introspected
@Serdeable
public class MoveOrganizationNodeResponse {

    @JsonProperty("id")
    @Schema(description = "Идентификатор записи в БД", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private final Long id;

    @JsonProperty("uuid")
    @Schema(description = "Не технический идентификатор звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "9c2c7a6d-29e9-4c8c-a0b3-3b14f7c2b4f1")
    private final UUID uuid;

    @JsonProperty("oldPath")
    @Schema(description = "Старый путь из идентификаторов от корня организации до текущего звена", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.100")
    private final String oldPath;

    @JsonProperty("newPath")
    @Schema(description = "Новый путь из идентификаторов от корня организации до текущего звена", requiredMode = Schema.RequiredMode.REQUIRED, example = "1.3.100")
    private final String newPath;

    @JsonProperty("version")
    @Schema(description = "Версия данных для оптимистичной блокировки", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private final Long version;


    @JsonProperty("movedDescendants")
    @Schema(description = "Перемещенные потомки", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<OrganizationNodeDTO> movedDescendants;

}
