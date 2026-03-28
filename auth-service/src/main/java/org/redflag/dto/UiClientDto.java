package org.redflag.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Set;
import java.util.UUID;

@Serdeable
@Introspected
public record UiClientDto(
        Long id,
        String login,
        UUID uuidDepartament,
        Set<String> roles) {
}
