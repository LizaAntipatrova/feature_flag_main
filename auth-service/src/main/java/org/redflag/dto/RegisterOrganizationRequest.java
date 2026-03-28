package org.redflag.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Serdeable
@Introspected
public record RegisterOrganizationRequest(
        @NotBlank
        String login,

        @NotBlank
        @Size(min = 8)
        String password,

        @NotBlank
        String organization_name
) {}