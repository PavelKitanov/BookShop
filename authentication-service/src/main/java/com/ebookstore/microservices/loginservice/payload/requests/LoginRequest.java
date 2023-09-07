package com.ebookstore.microservices.loginservice.payload.requests;

import lombok.Data;

@Data
public class LoginRequest {


    private String username;
    private String password;
}
