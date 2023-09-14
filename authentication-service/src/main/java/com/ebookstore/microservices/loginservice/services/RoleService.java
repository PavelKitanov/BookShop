package com.ebookstore.microservices.loginservice.services;

import com.ebookstore.microservices.loginservice.enumerations.Roles;
import com.ebookstore.microservices.loginservice.models.Role;

public interface RoleService {

    Role saveRole(Role role);
    Role findRoleByName(Roles name);
}
