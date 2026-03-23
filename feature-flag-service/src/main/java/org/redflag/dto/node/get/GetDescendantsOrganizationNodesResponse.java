package org.redflag.dto.node.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.redflag.dto.node.OrganizationNodeDTO;

import java.util.List;
import java.util.UUID;

@Data
@Introspected
@Serdeable
public class GetDescendantsOrganizationNodesResponse {

    @JsonProperty("nodeId")
    @Schema(description = "Идентификатор дочернего звена", requiredMode = Schema.RequiredMode.REQUIRED, example = "130")
    private final Long nodeId;

    @JsonProperty("depth")
    @Schema(description = "Максимальный уровень потомков от текущего", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private final Integer depth;

    @JsonProperty("items")
    @Schema(description = "Массив звеньев-предков", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<OrganizationNodeDTO> items;

}
