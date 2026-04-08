package org.redflag.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("serviceId")
    private UUID serviceId;
    @JsonProperty("topic")
    private String topic;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("groupName")
    private String groupName;
}
