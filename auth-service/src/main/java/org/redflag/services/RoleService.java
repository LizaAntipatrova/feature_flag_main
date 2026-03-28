package org.redflag.services;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.entities.Role;
import org.redflag.repositories.RoleRepository;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }


}