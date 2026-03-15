package org.redflag.dto.organization.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Introspected
@Serdeable
public class UpdateOrganizationRequest {
    @JsonIgnore
    private Long id;

    @JsonProperty("name")
    @Schema(description = "Новое название организации", requiredMode = Schema.RequiredMode.REQUIRED, example = "ООО Хихи-хаха")
    private final String name;
}
