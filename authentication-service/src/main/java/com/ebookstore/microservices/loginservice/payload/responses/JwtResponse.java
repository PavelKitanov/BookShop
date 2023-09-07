package com.ebookstore.microservices.loginservice.payload.responses;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;
    private Long userId;

    public JwtResponse(String accessToken, Long id, String username, String email, String role, Long userId) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }


    public JwtResponse(String token) {
        this.token = token;
    }
}
