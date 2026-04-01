package org.redflag.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;

@Serdeable
public record OrganizationNodeDTO(
        Long id,
        Long organizationId,
        UUID uuid,
        String path,
        String name,
        Boolean isService,
        Integer version
) {
}
