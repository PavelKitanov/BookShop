package com.ebookstore.microservices.bookservice.dto;

import com.ebookstore.microservices.bookservice.enumerations.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private Long userId;
    private Roles role;

}
