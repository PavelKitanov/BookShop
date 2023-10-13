package com.ebookstore.microservices.bookservice.proxy;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.exceptions.InvalidOrExpiredTokenException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="authentication-service")
public interface AuthenticationProxy {

    @GetMapping("/auth/validateToken")
    ResponseEntity<UserDto> validateToken(@RequestHeader("Authorization") String tokenHeader);
}
