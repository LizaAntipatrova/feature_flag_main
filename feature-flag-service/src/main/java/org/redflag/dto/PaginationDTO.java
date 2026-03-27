package org.redflag.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@RequiredArgsConstructor
@SuperBuilder
@Serdeable
@Introspected
public class PaginationDTO {
    @JsonProperty("limit")
    @Schema(description = "Верхний лимит количества записей для текущего массива", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    protected final Integer limit;

    @JsonProperty("offset")
    @Schema(description = "Начальный номер записи от начала для текущего массива", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    protected final Integer offset;
}