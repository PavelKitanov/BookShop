package com.ebookstore.microservices.bookservice.services;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface TokenService {

    ResponseEntity<UserDto> callValidateToken(String token);
}
