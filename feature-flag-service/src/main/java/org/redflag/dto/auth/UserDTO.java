package org.redflag.dto.auth;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.redflag.auth.Role;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Introspected
@Serdeable
public class UserDTO {
    private final Long id;
    private final String login;
    private final UUID uuidDepartament;
    private final Set<Role> roles;
}
