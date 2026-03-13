package org.redflag.dto.ff.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Introspected
@Serdeable
public class CreateFeatureFlagResponse {
    @JsonProperty("id")
    @Schema(description = "Идентификатор фича флага", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Long id;
    @JsonProperty("nodeId")
    @Schema(description = "Идентификатор звена организации, к которой принадлежит фича флаг", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private final Long nodeId;
    @JsonProperty("name")
    @Schema(description = "Название фича флага", requiredMode = Schema.RequiredMode.REQUIRED, example = "friday_release_mode")
    private final String name;
    @JsonProperty("value")
    @Schema(description = "Значение фича флага", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private final Boolean value;
    @JsonProperty("version")
    @Schema(description = "Версия данных для оптимистичной блокировки", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Long version;
}
