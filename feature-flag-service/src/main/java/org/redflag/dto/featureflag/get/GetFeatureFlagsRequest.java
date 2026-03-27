package org.redflag.dto.featureflag.get;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.PaginationDTO;

@Getter
@SuperBuilder
@Introspected
@Serdeable
public class GetFeatureFlagsRequest extends PaginationDTO {
    private final Long organizationId;
    private final Long nodeId;
}
