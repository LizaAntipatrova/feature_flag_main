package org.redflag.dto.node.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Introspected
@Serdeable
public class CreateOrganizationNodeRequest {
    @JsonProperty("name")
    @Schema(description = "Название звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "Кредитование")
    private final String name;
    @JsonProperty("isService")
    @Schema(description = "Является ли данное звено организации сервисом", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private final Boolean isService;
    @JsonProperty("parentId")
    @Schema(description = "Идентификатор родительского звена организации", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "100")
    private final Long parentId;
    @JsonIgnore
    private Long organizationId;
}
