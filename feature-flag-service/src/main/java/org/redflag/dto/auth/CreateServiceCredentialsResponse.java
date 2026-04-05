package org.redflag.dto.auth;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@Introspected
@Serdeable
public class CreateServiceCredentialsResponse {
    private UUID serviceId;
    private String topic;
    private String username;
    private String password;
    private String groupName;
}
