package org.redflag.dto.auth;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Introspected
@Serdeable
public class NodeUuidsDTO {
    private List<UUID> nodeUuids;
}
