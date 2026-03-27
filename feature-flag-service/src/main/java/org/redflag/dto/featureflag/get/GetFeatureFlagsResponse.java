package org.redflag.dto.featureflag.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.PaginationDTO;
import org.redflag.dto.featureflag.FeatureFlagDTO;

import java.util.List;

@SuperBuilder
@Getter
@Introspected
@Serdeable
public class GetFeatureFlagsResponse extends PaginationDTO {

    @JsonProperty("nodeId")
    @Schema(description = "Идентификатор звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Long nodeId;

    @JsonProperty("items")
    @Schema(description = "Массив фича флагов звена организации", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<FeatureFlagDTO> items;

    @JsonProperty("total")
    @Schema(description = "Фактическое количество записей", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Integer total;

}
