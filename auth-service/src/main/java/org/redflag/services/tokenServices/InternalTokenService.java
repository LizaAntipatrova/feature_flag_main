package org.redflag.services.tokenServices;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.constants.SecurityConstants;
import org.redflag.exception.ServerCustomException;

import java.util.Map;
import java.util.Set;

@Singleton
@RequiredArgsConstructor
public class InternalTokenService {

    private final JwtTokenGenerator tokenGenerator;

    public String generateInternalToken(String role, String principal) {
        Authentication authentication = Authentication.build(
                principal,
                Set.of(role)
        );

        return tokenGenerator.generateToken(authentication, SecurityConstants.EXPIRATION_TOKEN_SECONDS)
                .orElseThrow(() -> new ServerCustomException("Failed to generate JWT"));
    }
}
