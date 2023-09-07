package com.ebookstore.microservices.loginservice.dto;

import com.ebookstore.microservices.loginservice.models.Role;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;

    private String name;

    private String email;

    private String password;

    private Role role;
}
