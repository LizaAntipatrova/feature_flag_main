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
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.model.OrganizationNode;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@Getter
@Introspected
@Serdeable
public class GetLinkedFeatureFlagsResponse extends PaginationDTO {

    @JsonProperty("nodeId")
    @Schema(description = "Идентификатор звена организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Long nodeId;

    @JsonProperty("relation")
    @Schema(description = "Массив типов отношений с помощью которых связаны звенья организации", requiredMode = Schema.RequiredMode.REQUIRED, examples = {"self"})
    private final RelationType relation;

    @JsonProperty("items")
    @Schema(description = "Массив фича флагов с нодами", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<Item> items;

    @JsonProperty("total")
    @Schema(description = "Фактическое количество записей", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Integer total;

    @Data
    @Introspected
    @Serdeable
    public static class Item {

        @JsonProperty("featureFlag")
        @Schema(description = "Информация о фича флаге", requiredMode = Schema.RequiredMode.REQUIRED)
        private final FeatureFlagDTO featureFlag;

        @JsonProperty("belongsToNode")
        @Schema(description = "Информация о звене организации, к которой принадлежит фича флаг", requiredMode = Schema.RequiredMode.REQUIRED)
        private final OrganizationNodeDTO belongsToNode;
    }

}
