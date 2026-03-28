package org.redflag.services.sessionServices;

import org.redflag.exception.BadCredentialsCustomException;

public class SupportSessionUtils {

    public static Long parseSessionId(String sessionIdStr) {
        long sessionId;
        try {
            sessionId = Long.parseLong(sessionIdStr);
        } catch (NumberFormatException e) {
            throw new BadCredentialsCustomException("Invalid session format");
        }
        return sessionId;
    }
}
