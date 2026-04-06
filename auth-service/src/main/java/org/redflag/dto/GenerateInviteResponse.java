package org.redflag.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GenerateInviteResponse(
        String inviteToken
) {
}
