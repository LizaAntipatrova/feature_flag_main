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
public class UpdateOrganizationNodeRequest {
    @JsonProperty("name")
    @Schema(description = "Название звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "Кредитование")
    private final String name;
    @JsonProperty("isService")
    @Schema(description = "Является ли данное звено организации сервисом", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private final Boolean isService;
    @JsonProperty("version")
    @Schema(description = "Версия данных для оптимистичной блокировки", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Long version;
    @JsonIgnore
    private Long organizationId;
    @JsonIgnore
    private Long nodeId;

}
