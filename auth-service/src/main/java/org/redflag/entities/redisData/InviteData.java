package org.redflag.entities.redisData;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;
import java.util.UUID;

@Serdeable
public record InviteData (
        List<String> roles,
        UUID uuidDepartament
){
}
