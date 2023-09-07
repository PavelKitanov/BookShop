package com.ebookstore.microservices.bookservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="authentication-service")
public interface AuthenticationProxy {

    @GetMapping("/auth/validateToken")
    ResponseEntity<String> validateToken(@RequestHeader("Authorization") String tokenHeader);
}
