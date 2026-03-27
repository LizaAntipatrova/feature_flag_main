package org.redflag.dto.organization;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Serdeable
@Introspected
public class OrganizationIdDTO {
    private final Long organizationId;
}
