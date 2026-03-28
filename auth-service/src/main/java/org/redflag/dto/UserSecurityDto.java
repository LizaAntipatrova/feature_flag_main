package org.redflag.dto;

import java.util.List;
import java.util.UUID;

public record UserSecurityDto(
        Long id,
        String login,
        UUID uuidDepartament,
        List<String> roles) {
}
