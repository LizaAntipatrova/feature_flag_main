package org.redflag.dto.node.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.PaginationDTO;
import org.redflag.dto.node.OrganizationNodeDTO;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@Getter
@Introspected
@Serdeable
public class GetOrganizationNodesResponse extends PaginationDTO {

    @JsonProperty("items")
    @Schema(description = "Массив звеньев организации", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<OrganizationNodeDTO> items;

    @JsonProperty("total")
    @Schema(description = "Фактическое количество записей", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Integer total;


}
