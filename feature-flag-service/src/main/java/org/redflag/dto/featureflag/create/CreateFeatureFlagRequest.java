package org.redflag.dto.featureflag.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Introspected
@Serdeable
public class CreateFeatureFlagRequest {
    @JsonProperty("name")
    @Schema(description = "Название фича флага", requiredMode = Schema.RequiredMode.REQUIRED, example = "friday_release_mode")
    private final String name;
    @JsonProperty("value")
    @Schema(description = "Значение фича флага", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private final Boolean value;
    @JsonIgnore
    private Long organizationId;
    @JsonIgnore
    private Long nodeId;
}
