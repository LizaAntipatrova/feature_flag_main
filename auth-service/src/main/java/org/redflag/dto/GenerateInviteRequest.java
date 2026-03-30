package org.redflag.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Serdeable
public record GenerateInviteRequest(
        @NotNull(message = "The list of roles cannot be missing")
        @NotEmpty(message = "The list of roles cannot be empty")
        List<String> roles,

        @NotNull
        UUID uuidDepartament
) {
}
