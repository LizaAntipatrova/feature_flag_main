package org.redflag.dto.featureflag.get;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.node.OrganizationNodeIdDTO;

@SuperBuilder
@Getter
@Introspected
@Serdeable
public class GetFeatureFlagByNameRequest extends OrganizationNodeIdDTO {
    private final String flagName;
}
