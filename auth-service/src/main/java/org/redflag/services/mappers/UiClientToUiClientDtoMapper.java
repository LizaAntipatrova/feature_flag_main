package org.redflag.services.mappers;

import jakarta.inject.Singleton;
import org.redflag.dto.UiClientDto;
import org.redflag.entities.Role;
import org.redflag.entities.UiClient;

import java.util.stream.Collectors;

@Singleton
public class UiClientToUiClientDtoMapper {

    public UiClientDto mapToUiClientDto(UiClient client) {
        return new UiClientDto(
                client.getId(),
                client.getLogin(),
                client.getUuidDepartament(),
                client.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}
