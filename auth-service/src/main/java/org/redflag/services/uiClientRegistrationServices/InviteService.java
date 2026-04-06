package org.redflag.services.uiClientRegistrationServices;

import io.lettuce.core.api.StatefulRedisConnection;
import io.micronaut.context.annotation.Property;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.constants.InvitingConstants;
import org.redflag.dto.GenerateInviteRequest;
import org.redflag.dto.GenerateInviteResponse;
import org.redflag.entities.redisData.InviteData;
import org.redflag.exception.AccessDeniedCustomException;
import org.redflag.exception.ResourceNotFoundCustomException;
import org.redflag.exception.ServerCustomException;
import org.redflag.services.UiClientServices.CheckSubordinationService;

import java.io.IOException;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class InviteService {

    private final StatefulRedisConnection<String, String> redisConnection;
    private final ObjectMapper objectMapper;
    private final CheckSubordinationService checkSubordinationService;

    public GenerateInviteResponse generateInvite(Authentication authentication, GenerateInviteRequest request) {
        if (!checkSubordinationService.isSubordinatedFromAuth(authentication, request.uuidDepartament())) {
            throw new AccessDeniedCustomException("Access denied. The department you want to put is not a subordinate");
        }

        String token = UUID.randomUUID().toString();
        InviteData data = new InviteData(request.roles(), request.uuidDepartament());

        try {
            String jsonValue = objectMapper.writeValueAsString(data);
            var commands = redisConnection.sync();
            commands.set(token, jsonValue);
            commands.expire(token, InvitingConstants.EXPIRE_INVITATION);

            return new GenerateInviteResponse(token);
        } catch (IOException e) {
            throw new ServerCustomException("Error creating JSON for Redis");
        }
    }

    public InviteData getInviteData(String token) {
        String jsonValue = redisConnection.sync().get(token);
        if (jsonValue == null) {
            throw new ResourceNotFoundCustomException("The link is invalid or has expired");
        }

        try {
            return objectMapper.readValue(jsonValue, InviteData.class);
        } catch (IOException e) {
            throw new ServerCustomException("Error reading data from Redis");
        }
    }

    public void deleteToken(String token) {
        redisConnection.sync().del(token);
    }
}
