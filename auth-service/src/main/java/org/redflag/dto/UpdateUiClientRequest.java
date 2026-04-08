package org.redflag.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.*;

@Serdeable @Introspected
public record UpdateUiClientRequest(

        @NotBlank
        String oldPassword,

        @Nullable
        String newLogin,

        @Nullable
        @Size(min = 8)
        String newPassword
) {}