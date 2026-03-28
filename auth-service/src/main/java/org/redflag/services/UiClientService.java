package org.redflag.services;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.UiClientDto;
import org.redflag.dto.UpdateUiClientRequest;
import org.redflag.entities.Role;
import org.redflag.entities.UiClient;
import org.redflag.exception.AccessDeniedCustomException;
import org.redflag.exception.BadCredentialsCustomException;
import org.redflag.exception.ConflictCustomException;
import org.redflag.exception.ResourceNotFoundCustomException;
import org.redflag.repositories.RoleRepository;
import org.redflag.repositories.UiClientRepository;
import org.redflag.services.mappers.UiClientToUiClientDtoMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class UiClientService {

    private final UiClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UiClientToUiClientDtoMapper mapper;

    public List<UiClientDto> getAllByDepartment(UUID uuidDepartament) {
        if (uuidDepartament == null) {
            throw new BadCredentialsCustomException("Department UUID cannot be null");
        }

        List<UiClient> clients = clientRepository.findByUuidDepartament(uuidDepartament);

        if (clients.isEmpty()) {
            throw new ResourceNotFoundCustomException("No clients found for department: " + uuidDepartament);
        }

        return clients.stream()
                .map(mapper::mapToUiClientDto)
                .toList();
    }

    public UiClientDto getByLogin(String login) {
        return clientRepository.findByLogin(login)
                .map(mapper::mapToUiClientDto)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found"));
    }

    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    @Transactional
    public void updateUiClient(String login, UpdateUiClientRequest request) {
        UiClient client = clientRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), client.getPassword())) {
            throw new AccessDeniedCustomException("Invalid old password");
        }

        if (request.newLogin() != null && !request.newLogin().isBlank()) {
            if (!client.getLogin().equals(request.newLogin())) {
                if (clientRepository.findByLogin(request.newLogin()).isPresent()) {
                    throw new ConflictCustomException("Login already taken");
                }
                client.setLogin(request.newLogin());
            }
        }

        if (request.newPassword() != null && !request.newPassword().isBlank()) {
            client.setPassword(passwordEncoder.encode(request.newPassword()));
        }

        clientRepository.update(client);
    }

    @Transactional
    public void addRoles(Long userId, Set<String> roleNames) {
        UiClient client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found"));

        Set<Role> rolesToAdd = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundCustomException("Role not found: " + name)))
                .collect(Collectors.toSet());

        client.getRoles().addAll(rolesToAdd);
        clientRepository.update(client);
    }

    @Transactional
    public void removeRoles(Long userId, Set<String> roleNames) {
        UiClient client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found"));

        client.getRoles().removeIf(role -> roleNames.contains(role.getName()));

        clientRepository.update(client);
    }

    @Transactional
    public void updateDepartment(Long userId, UUID departmentUuid) {
        UiClient client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found"));

        client.setUuidDepartament(departmentUuid);
        clientRepository.update(client);
    }
}