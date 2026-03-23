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
public class GetChildrenOrganizationNodesResponse {

    @JsonProperty("nodeId")
    @Schema(description = "Идентификатор родительского звена", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private final Long nodeId;

    @JsonProperty("items")
    @Schema(description = "Массив дочерних звеньев", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<OrganizationNodeDTO> items;

}
