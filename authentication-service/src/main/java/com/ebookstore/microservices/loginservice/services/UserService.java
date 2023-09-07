package com.ebookstore.microservices.loginservice.services;

import com.ebookstore.microservices.loginservice.dto.UserDto;
import com.ebookstore.microservices.loginservice.models.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    User findUserByEmail(String email);

    List<User> findAllUsers();

    User loginUser(String email, String password);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findUserByUsername(String username);
}
