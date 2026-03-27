package org.redflag.dto.featureflag;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Builder
@Getter
@Serdeable
@Introspected
public class FeatureFlagIdDTO {
    private final Long organizationId;
    private final Long nodeId;
    private final Long flagId;
}