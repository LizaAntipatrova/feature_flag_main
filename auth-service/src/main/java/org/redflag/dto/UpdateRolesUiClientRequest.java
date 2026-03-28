package org.redflag.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Serdeable
@Introspected
public record UpdateRolesUiClientRequest(
        @NotEmpty
        Set<String> roleNames
) {}