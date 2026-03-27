package org.redflag.dto.organization.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.PaginationDTO;
import org.redflag.dto.organization.OrganizationDTO;

import java.util.List;

@Getter
@SuperBuilder
@Introspected
@Serdeable
public class GetOrganizationsResponse extends PaginationDTO {

    @JsonProperty("items")
    @Schema(description = "Массив организаций", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<OrganizationDTO> items;
    @JsonProperty("total")
    @Schema(description = "Фактическое количество записей", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private final Integer total;

}
