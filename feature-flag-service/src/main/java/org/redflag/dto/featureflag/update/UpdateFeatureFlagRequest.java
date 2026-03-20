package org.redflag.dto.featureflag.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Introspected
@Serdeable
public class UpdateFeatureFlagRequest {
    @JsonProperty("value")
    @Schema(description = "Значение фича флага", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private final Boolean value;
    @JsonProperty("version")
    @Schema(description = "Версия данных для оптимистичной блокировки", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Long version;
    @JsonIgnore
    private Long organizationId;
    @JsonIgnore
    private Long nodeId;
    @JsonIgnore
    private Long featureFlagId;
}
