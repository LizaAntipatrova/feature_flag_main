package org.redflag.services;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import io.micronaut.transaction.annotation.Transactional;
import org.redflag.dto.RegisterOrganizationRequest;
import org.redflag.entities.Role;
import org.redflag.entities.UiClient;
import org.redflag.exception.ConflictCustomException;
import org.redflag.exception.ResourceNotFoundCustomException;
import org.redflag.repositories.RoleRepository;
import org.redflag.repositories.UiClientRepository;
import org.redflag.services.externalServices.FeatureFlagServiceClient;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

// переделать!!!
@Singleton
@RequiredArgsConstructor
public class RegistrationService {

    private final UiClientRepository uiClientRepository;
    private final RoleRepository roleRepository;
    private final FeatureFlagServiceClient featureFlagServiceClient;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerOrganization(RegisterOrganizationRequest request) {
        // 1. Проверка на уникальность (Используем кастомный Conflict)
        if (uiClientRepository.existsByLogin(request.login())) {
            throw new ConflictCustomException(String.format("Login '%s' is already taken", request.login()));
        }

        // 2. Вызов внешнего сервиса
        UUID departmentUuid = featureFlagServiceClient.createOrganizationNode(request.organization_name());

        // 3. Получение роли (Используем кастомный NotFound, если роль внезапно пропала)
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new ResourceNotFoundCustomException("Роль ADMIN не найдена в системе"));

        // 4. Создание пользователя
        UiClient newUser = new UiClient();
        newUser.setLogin(request.login());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setUuidDepartament(departmentUuid);
        newUser.setRoles(Set.of(adminRole));

        uiClientRepository.save(newUser);
    }
}