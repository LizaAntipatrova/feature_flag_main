package org.redflag.dto.featureflag.get;

import io.micronaut.serde.annotation.Serdeable;
import lombok.RequiredArgsConstructor;

@Serdeable
@RequiredArgsConstructor
public enum RelationType {
    SELF,
    ANCESTOR,
    DESCENDANT
}