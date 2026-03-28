package org.redflag.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import org.redflag.entities.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@Serdeable
public record SessionResponseDto(

        String login,
        UUID uuidDepartament,
        Set<Role> roles
) {

}