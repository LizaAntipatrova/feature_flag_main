package org.redflag.services.tokenServices;

import org.redflag.exception.BadCredentialsCustomException;

import java.util.UUID;

public class SupportTokenUtils {

    public static UUID parseUuidOrThrow(String login) {
        try {
            return UUID.fromString(login);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsCustomException("The login must be a valid UUID (e.g., 550e8400-e29b-41d4-a716-446655440000)");
        }
    }
}
