package com.ebookstore.microservices.loginservice.web;


import com.ebookstore.microservices.loginservice.enumerations.Roles;
import com.ebookstore.microservices.loginservice.models.Role;
import com.ebookstore.microservices.loginservice.services.RoleService;
import com.ebookstore.microservices.loginservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private final RoleService roleService;

    @Autowired
    private final UserService userService;

    public RoleController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> findAll(@RequestHeader("Authorization") String tokenHeader){
        userService.validateToken(tokenHeader);
        return ResponseEntity.ok(roleService.findAll());
    }

    @PostMapping("/createRole")
    public Role createRole(@RequestHeader("Authorization") String tokenHeader,
                           @RequestParam Roles role){
        userService.validateToken(tokenHeader);
        return roleService.saveRole(new Role(role));
    }
}
