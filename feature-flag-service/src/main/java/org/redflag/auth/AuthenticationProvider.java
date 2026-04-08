package org.redflag.auth;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.utils.SecurityService;
import jakarta.annotation.Nonnull;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.error.ErrorCatalog;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class AuthenticationProvider {
    public static final String NODE_UUID_ATTRIBUTE_NAME = "nodeUuid";
    public static final String SESSION_COOKIE_NAME = "SESSION";
    private final SecurityService service;

    public UUID getAuthenticationNodeUuid(){
        Authentication authentication = getAuthentication();
        return UUID.fromString(
                authentication.getAttributes().get(NODE_UUID_ATTRIBUTE_NAME).toString());
    }


    private Authentication getAuthentication() {
        return service.getAuthentication()
                .orElseThrow(ErrorCatalog.UNAUTHORIZED::getException);
    }
}
