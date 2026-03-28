package org.redflag.dto;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import java.util.UUID;

@Serdeable
public record SdkClientResponse(

        Long id,

        UUID login,

        @Nullable
        String password
) {}