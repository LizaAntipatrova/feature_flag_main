package org.redflag.exception.handler;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Serdeable
public class CustomErrorResponse {

    private String message;
    private Map<String, Object> info;

}