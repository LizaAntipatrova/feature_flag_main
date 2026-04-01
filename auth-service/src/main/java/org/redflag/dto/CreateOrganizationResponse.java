package org.redflag.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

import java.util.UUID;

@Serdeable
public record CreateOrganizationResponse (

        Long id,
        String name,
        OrganizationNodeDTO organizationNode
){
}
