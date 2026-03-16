package org.redflag.dto.node.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Introspected
@Serdeable
public class MoveOrganizationNodeRequest {
    @JsonIgnore
    private Long organizationId;
    @JsonIgnore
    private Long nodeId;

    @JsonProperty("newParentId")
    @Schema(description = "идентификатор, родителя к которому перемещаем звено организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    private final Long newParentId;

    @JsonProperty("version")
    @Schema(description = "Версия данных для оптимистичной блокировки", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Long version;
}
