package org.redflag.dto.complex;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.featureflag.FeatureFlagDTO;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@Getter
@Introspected
@Serdeable
public class GetFeatureFlagsByUuidServiceNodeResponse {
    @JsonProperty("nodeUuid")
    @Schema(description = "Не технический идентификатор звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final UUID nodeUuid;

    @JsonProperty("items")
    @Schema(description = "Массив фича флагов звена организации", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<FeatureFlagDTO> items;

    @JsonProperty("total")
    @Schema(description = "Фактическое количество записей", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Integer total;

}
