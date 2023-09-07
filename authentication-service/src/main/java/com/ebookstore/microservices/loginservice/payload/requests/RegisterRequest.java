package com.ebookstore.microservices.loginservice.payload.requests;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;

    private String email;

    private String password;

    private String role;
}
