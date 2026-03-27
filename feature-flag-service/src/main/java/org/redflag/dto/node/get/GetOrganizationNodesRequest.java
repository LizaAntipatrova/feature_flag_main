package org.redflag.dto.node.get;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.redflag.dto.PaginationDTO;

import java.lang.ref.PhantomReference;

@SuperBuilder
@Getter
@Introspected
@Serdeable
public class GetOrganizationNodesRequest extends PaginationDTO {
    private final Long organizationId;
    private final Long nodeId;
}
