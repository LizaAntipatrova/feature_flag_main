package org.redflag.services.mappers;

import jakarta.inject.Singleton;
import org.redflag.dto.UserSecurityDto;
import org.redflag.entities.Role;
import org.redflag.entities.UiClient;

@Singleton
public class UiClientToUserSecurityDtoMapper {

    public UserSecurityDto mapToDto(UiClient user) {
        return new UserSecurityDto(
                user.getId(),
                user.getLogin(),
                user.getUuidDepartament(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}
