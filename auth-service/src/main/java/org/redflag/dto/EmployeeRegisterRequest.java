package org.redflag.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record EmployeeRegisterRequest(
        @NotBlank
        String login,

        @NotBlank
        String password

) {
}
