package org.redflag.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Serdeable
@Introspected
public record SdkLoginRequest(

        @NotNull
        UUID newLogin
) {}