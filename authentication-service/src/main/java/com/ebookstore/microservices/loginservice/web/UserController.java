package com.ebookstore.microservices.loginservice.web;

import com.ebookstore.microservices.loginservice.dto.UserDto;
import com.ebookstore.microservices.loginservice.models.Role;
import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(@RequestHeader("Authorization") String tokenHeader){
        userService.validateToken(tokenHeader);
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserById(@RequestHeader("Authorization") String tokenHeader,
                                             @PathVariable Long userId){
        userService.validateToken(tokenHeader);
        User user = userService.findUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/role")
    public ResponseEntity<Role> findUserRole(@RequestHeader("Authorization") String tokenHeader,
                                             @PathVariable Long userId){
        userService.validateToken(tokenHeader);
        Role role = userService.getUserRole(userId);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/updateRole")
    public ResponseEntity<User> updateRole(@RequestHeader("Authorization") String tokenHeader,
                                           @RequestParam Long userId,
                                           @RequestParam String role){
        userService.validateToken(tokenHeader);
        User user = userService.updateRole(userId, role);

        return ResponseEntity.ok(user);
    }
}
