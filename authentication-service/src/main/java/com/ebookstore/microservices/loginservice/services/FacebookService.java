package com.ebookstore.microservices.loginservice.services;

import com.ebookstore.microservices.loginservice.models.facebook.FacebookUser;
import com.ebookstore.microservices.loginservice.payload.responses.JwtResponse;
import com.ebookstore.microservices.loginservice.models.User;


public interface FacebookService {
    JwtResponse loginUser(String fbAccessToken);
    User convertTo(FacebookUser facebookUser);
    String generateUsername(String username);
    String generatePassword(int length);
}
