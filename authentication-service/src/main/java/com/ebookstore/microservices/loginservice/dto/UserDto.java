package com.ebookstore.microservices.loginservice.dto;

import com.ebookstore.microservices.loginservice.enumerations.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserDto {

    private Long userId;
    private Roles role;

}
