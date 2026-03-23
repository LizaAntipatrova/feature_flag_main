package org.redflag.dto.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@RequiredArgsConstructor
@Introspected
@Serdeable
public class OrganizationNodeIdDTO {
    private final Long organizationId;
    private final Long nodeId;
}
