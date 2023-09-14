package com.ebookstore.microservices.loginservice.services.impl;

import com.ebookstore.microservices.loginservice.enumerations.Roles;
import com.ebookstore.microservices.loginservice.models.Role;
import com.ebookstore.microservices.loginservice.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements com.ebookstore.microservices.loginservice.services.RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findRoleByName(Roles name) {
        return roleRepository.findRoleByName(name);
    }
}
