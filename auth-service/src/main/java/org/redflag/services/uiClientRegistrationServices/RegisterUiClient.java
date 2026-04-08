package org.redflag.services.uiClientRegistrationServices;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.RegisterOrganizationRequest;
import org.redflag.entities.Role;
import org.redflag.entities.UiClient;
import org.redflag.exception.ConflictCustomException;
import org.redflag.repositories.RoleRepository;
import org.redflag.repositories.UiClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class RegisterUiClient {

    private final UiClientRepository uiClientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUiUser(RegisterOrganizationRequest request, UUID departmentUuid){
        if (uiClientRepository.existsByLogin(request.login())) {
            throw new ConflictCustomException(String.format("Login '%s' is already taken", request.login()));
        }
        Set<Role> roles = new HashSet<>(roleRepository.findAll());

        UiClient newUser = new UiClient();
        newUser.setLogin(request.login());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setUuidDepartament(departmentUuid);
        newUser.setRoles(roles);

        uiClientRepository.save(newUser);

    }
}
