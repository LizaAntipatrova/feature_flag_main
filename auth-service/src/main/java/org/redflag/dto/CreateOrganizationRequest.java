package org.redflag.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CreateOrganizationRequest(
        String name
) {}