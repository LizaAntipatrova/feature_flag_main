package org.redflag.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Serdeable
public record DeleteListUiClientsRequest(

        @NotNull
        List<Long> ids
) {
}
