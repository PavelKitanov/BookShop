package com.ebookstore.microservices.loginservice.models.google;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUser {
    private String Id;
    private String name;
    private String email;
}
