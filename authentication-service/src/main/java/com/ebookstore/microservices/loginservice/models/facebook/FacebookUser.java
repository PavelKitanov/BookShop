package com.ebookstore.microservices.loginservice.models.facebook;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacebookUser {

    private String id;
    private String name;
    private String email;
}
