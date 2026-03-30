package org.redflag.controllers;

import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redflag.annotations.NoSdkAllowed;
import org.redflag.entities.Role;
import org.redflag.services.RoleService;

import java.util.List;

@Controller("/api/v1/roles")
@RequiredArgsConstructor
@NoSdkAllowed
@Tag(name = "CRUD методы для сущности Роль")
public class RoleController {

    private final RoleService roleService;

    @Get
    public List<Role> getAllRoles() {
        return roleService.findAll();
    }

}