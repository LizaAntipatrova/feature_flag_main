package org.redflag.dto.node.get;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.node.OrganizationNodeIdDTO;

@SuperBuilder
@Getter
@Introspected
@Serdeable
public class GetDescendantsOrganizationNodesRequest extends OrganizationNodeIdDTO {
    private final Integer depth;
}
