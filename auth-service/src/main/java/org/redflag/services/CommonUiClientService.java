package org.redflag.services;

import io.micronaut.security.authentication.Authentication;
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
import org.redflag.services.UiClientServices.CheckSubordinationService;
import org.redflag.services.mappers.UiClientToUiClientDtoMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class CommonUiClientService {

    private final UiClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UiClientToUiClientDtoMapper mapper;
    private final CheckSubordinationService checkSubordinationService;

    public List<UiClientDto> getAllByDepartment(Authentication authentication, UUID uuidDepartament) {
        if (uuidDepartament == null) {
            throw new BadCredentialsCustomException("Department UUID cannot be null");
        }
        if (!checkSubordinationService.isSubordinatedFromAuth(authentication, uuidDepartament)) {
            throw new AccessDeniedCustomException("Access denied. The department you want to view is not a subordinate");
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

    public void delete(Authentication authentication, Long id) {
        UUID uuidDepartament = clientRepository.findUuidDepartamentById(id)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User does not exist"));

        if (!checkSubordinationService.isSubordinatedFromAuth(authentication, uuidDepartament)) {
            throw new AccessDeniedCustomException("Access denied. The user is not a subordinate");
        }
        clientRepository.deleteById(id);
    }

    public void deleteListUiClientsByDepartment(List<UUID> departmentUuids) {
        long count = clientRepository.countByUuidDepartamentIn(departmentUuids);
        if (count == 0) {
            throw new ResourceNotFoundCustomException("No clients found");
        }
        clientRepository.deleteAllByUuidDepartamentIn(departmentUuids);
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
    public void addRoles(Authentication authentication, Long userId, Set<String> roleNames) {
        UiClient client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found"));

        if (!checkSubordinationService.isSubordinatedFromAuth(authentication, client.getUuidDepartament())) {
            throw new AccessDeniedCustomException("Access denied. The user is not a subordinate");
        }

        Set<Role> rolesToAdd = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundCustomException("Role not found: " + name)))
                .collect(Collectors.toSet());

        client.getRoles().addAll(rolesToAdd);
        clientRepository.update(client);
    }

    @Transactional
    public void removeRoles(Authentication authentication, Long userId, Set<String> roleNames) {
        UiClient client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found"));

        if (!checkSubordinationService.isSubordinatedFromAuth(authentication, client.getUuidDepartament())) {
            throw new AccessDeniedCustomException("Access denied. The user is not a subordinate");
        }

        client.getRoles().removeIf(role -> roleNames.contains(role.getName()));

        clientRepository.update(client);
    }

    @Transactional
    public void updateDepartment(Authentication authentication, Long userId, UUID departmentUuid) {
        UiClient client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found"));

        if (!checkSubordinationService.isSubordinatedFromAuth(authentication, client.getUuidDepartament())) {
            throw new AccessDeniedCustomException("Access denied. The department you want to update is not a subordinate");
        }

        client.setUuidDepartament(departmentUuid);
        clientRepository.update(client);
    }
}