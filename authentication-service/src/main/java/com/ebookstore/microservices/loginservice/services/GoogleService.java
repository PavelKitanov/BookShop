package com.ebookstore.microservices.loginservice.services;

import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.models.google.GoogleUser;
import com.ebookstore.microservices.loginservice.payload.responses.JwtResponse;

public interface GoogleService {
    JwtResponse loginUser(String fbAccessToken);
    User convertTo(GoogleUser googleUser);
    String generateUsername(String username);
    String generatePassword(int length);
}
