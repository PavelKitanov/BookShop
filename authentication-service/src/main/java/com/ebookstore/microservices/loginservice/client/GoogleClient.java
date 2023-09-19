package com.ebookstore.microservices.loginservice.client;

import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.models.facebook.FacebookUser;
import com.ebookstore.microservices.loginservice.models.google.GoogleUser;
import com.ebookstore.microservices.loginservice.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GoogleClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String GOOGLE_API = "https://www.googleapis.com/oauth2/v2/userinfo";

    public GoogleUser getUser(String accessToken) {
        var path = "?access_token={access_token}";
        final Map<String, String> variables = new HashMap<>();
        variables.put("access_token", accessToken);
        return restTemplate
                .getForObject(GOOGLE_API + path, GoogleUser.class, variables);
    }

}
