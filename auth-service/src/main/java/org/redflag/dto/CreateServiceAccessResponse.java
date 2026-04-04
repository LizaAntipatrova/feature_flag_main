package org.redflag.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Serdeable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceAccessResponse {
    private String serviceId;
    private String topic;
    private String username;
    private String password;
    private String groupName;
}
