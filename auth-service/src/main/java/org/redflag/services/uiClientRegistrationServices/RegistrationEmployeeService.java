package org.redflag.services.uiClientRegistrationServices;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redflag.dto.EmployeeRegisterRequest;
import org.redflag.entities.Role;
import org.redflag.entities.UiClient;
import org.redflag.entities.redisData.InviteData;
import org.redflag.exception.ConflictCustomException;
import org.redflag.repositories.RoleRepository;
import org.redflag.repositories.UiClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class RegistrationEmployeeService {

    private final UiClientRepository uiClientRepository;
    private final RoleRepository roleRepository;
    private final InviteService inviteService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerEmployee(EmployeeRegisterRequest request, String token) {

        InviteData inviteData = inviteService.getInviteData(token);

        if (uiClientRepository.existsByLogin(request.login())) {
            throw new ConflictCustomException("The login is already taken");
        }

        List<Role> roles = roleRepository.findByNameIn(inviteData.roles());

        UiClient employee = new UiClient();
        employee.setLogin(request.login());
        employee.setPassword(passwordEncoder.encode(request.password()));
        employee.setUuidDepartament(inviteData.uuidDepartament());
        employee.setRoles(new HashSet<>(roles));

        uiClientRepository.save(employee);

        inviteService.deleteToken(token);
    }
}
