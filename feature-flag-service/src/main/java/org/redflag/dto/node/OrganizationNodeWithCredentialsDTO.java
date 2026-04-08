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
    @JsonProperty("password")
    @Schema(description = "Пароль сервиса для авторизации", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "7kg72BXF")
    private String password;
    @JsonProperty("topicName")
    @Schema(description = "Имя топика кафки для сервиса", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "svc.f848530b-84c8-4a4e-80b7-4d4e10d50f8d.events")
    private String topicName;
    @JsonProperty("groupName")
    @Schema(description = "Имя консьюмер группы для сервиса", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "grp.svc.f848530b-84c8-4a4e-80b7-4d4e10d50f8d")
    private String groupName;
}
