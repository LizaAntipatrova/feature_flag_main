package org.redflag.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Getter;
import org.redflag.auth.Role;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Introspected
@Serdeable
public class UserDTO {
    @JsonProperty("id")
    private final Long id;
    @JsonProperty("login")
    private final String login;
    @JsonProperty("uuidDepartament")
    private final UUID uuidDepartment;
    @JsonProperty("roles")
    private final Set<Role> roles;
}
