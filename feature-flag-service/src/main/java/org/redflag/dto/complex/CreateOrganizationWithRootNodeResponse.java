package org.redflag.dto.complex;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.node.OrganizationNodeDTO;
import org.redflag.dto.organization.OrganizationDTO;

import java.util.UUID;

@SuperBuilder
@Getter
@Introspected
@Serdeable
public class CreateOrganizationWithRootNodeResponse extends OrganizationDTO {

    @JsonProperty("organizationNode")
    @Schema(description = "Созданное корневое звено организации", requiredMode = Schema.RequiredMode.REQUIRED)
    private final OrganizationNodeDTO organizationNode;



}
