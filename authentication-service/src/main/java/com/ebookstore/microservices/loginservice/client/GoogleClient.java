package com.ebookstore.microservices.loginservice.client;

import com.ebookstore.microservices.loginservice.models.google.GoogleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class GoogleClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String GOOGLE_API = "https://www.googleapis.com/oauth2/v2/userinfo";

    public GoogleUser getUser(String accessToken) {
        var path = "?access_token={access_token}";
        final Map<String, String> variables = new HashMap<>();
        variables.put("access_token", accessToken);
        return restTemplate
                .getForObject(GOOGLE_API + path, GoogleUser.class, variables);
    }

}
