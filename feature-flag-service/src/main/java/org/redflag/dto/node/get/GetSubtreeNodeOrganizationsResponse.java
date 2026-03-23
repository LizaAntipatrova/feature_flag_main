package org.redflag.dto.node.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.node.OrganizationNodeDTO;

import java.util.List;
import java.util.UUID;

@Getter
@SuperBuilder
@Introspected
@Serdeable
public class GetSubtreeNodeOrganizationsResponse extends OrganizationNodeDTO {

    @JsonProperty("children")
    @Schema(description = "Поддеревья детей звена организации", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<GetSubtreeNodeOrganizationsResponse> children;
}
