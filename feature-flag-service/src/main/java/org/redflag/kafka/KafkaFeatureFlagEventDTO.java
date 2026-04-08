package org.redflag.kafka;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.protocol.types.Field;

@Builder
@Getter
@Introspected
@Serdeable
public class KafkaFeatureFlagEventDTO {
    private String flagName;
    private Boolean value;
}
