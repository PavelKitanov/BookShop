package com.ebookstore.microservices.loginservice.services.impl;

import com.ebookstore.microservices.loginservice.enumerations.Roles;
import com.ebookstore.microservices.loginservice.models.Role;
import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.repositories.RoleRepository;
import com.ebookstore.microservices.loginservice.repositories.UserRepository;
import com.ebookstore.microservices.loginservice.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = passwordEncoder.encode(user.getPassword());
        Role role = roleService.findRoleByName(Roles.ROLE_USER);
        return userRepository.save(new User(username, email, password, role));
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User loginUser(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if(user == null){
            throw new RuntimeException("There is no User registered with this email" + email);
        }
        boolean correctPassword = passwordEncoder.matches(password, user.getPassword());
        if(!correctPassword)
            throw new RuntimeException("The password is incorrect!");

        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }


}
