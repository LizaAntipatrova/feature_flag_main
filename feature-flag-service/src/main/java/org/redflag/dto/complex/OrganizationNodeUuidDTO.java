package org.redflag.dto.complex;

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
public class OrganizationNodeUuidDTO {
    @JsonProperty("nodeUuid")
    private final UUID nodeUuid;
}
