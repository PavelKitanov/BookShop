package com.ebookstore.microservices.loginservice.services;


import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.payload.responses.JwtResponse;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user, String role);

    void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;
    User findUserByEmail(String email);

    List<User> findAllUsers();

    JwtResponse loginUser(String username, String password);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByFacebookId(String facebookId);

    boolean verify(String verificationCode);

    User validateToken(String tokenHeader);

}
