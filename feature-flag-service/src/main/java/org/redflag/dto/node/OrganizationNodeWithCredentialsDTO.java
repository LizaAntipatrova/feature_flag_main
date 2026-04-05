package org.redflag.dto.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@Accessors(chain = true)
@Introspected
@Serdeable
public class OrganizationNodeWithCredentialsDTO extends OrganizationNodeDTO {
    @JsonProperty("username")
    @Schema(description = "Имя пользователя-сервиса для авторизации", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "9c2c7a6d-29e9-4c8c-a0b3-3b14f7c2b4f1")
    private String username;
    private String password;
    private String topicName;
    private String groupName;
}
