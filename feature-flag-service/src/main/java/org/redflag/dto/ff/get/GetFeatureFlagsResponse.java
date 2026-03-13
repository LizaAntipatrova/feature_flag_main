package org.redflag.dto.ff.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.redflag.dto.node.get.GetOrganizationNodesResponse;

import java.util.List;

@Data
@Introspected
@Serdeable
public class GetFeatureFlagsResponse {
    @JsonProperty("nodeId")
    @Schema(description = "Идентификатор звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Long nodeId;
    @JsonProperty("items")
    @Schema(description = "Массив фича флагов звена организации", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<FeatureFlag> items;
    @JsonProperty("limit")
    @Schema(description = "Верхний лимит количества записей для текущего массива", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private final Integer limit;
    @JsonProperty("offset")
    @Schema(description = "Начальный номер записи от начала для текущего массива", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private final Integer offset;
    @JsonProperty("total")
    @Schema(description = "Фактическое количество записей", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Integer total;

    @Data
    @Introspected
    @Serdeable
    public static class FeatureFlag{
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

}
