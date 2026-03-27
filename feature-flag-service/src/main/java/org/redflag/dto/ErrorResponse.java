package org.redflag.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Introspected
@Serdeable
public class ErrorResponse {

    @JsonProperty("code")
    @Schema(description = "Уникальный идентификатор ошибки", requiredMode = Schema.RequiredMode.REQUIRED, example = "00-0000")
    private String code;

    @JsonProperty("errorType")
    @Schema(description = "Категория ошибки", requiredMode = Schema.RequiredMode.REQUIRED, example = "Неожиданная ошибка выполнения")
    private String errorType;

    @JsonProperty("message")
    @Schema(description = "Сообщение ошибки", requiredMode = Schema.RequiredMode.REQUIRED, example = "Index 1 out of bounds for length 0")
    private String message;

}
