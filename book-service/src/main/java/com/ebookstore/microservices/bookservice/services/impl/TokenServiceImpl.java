package com.ebookstore.microservices.bookservice.services.impl;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.exceptions.InvalidOrExpiredTokenException;
import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import com.ebookstore.microservices.bookservice.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private final AuthenticationProxy authenticationProxy;

    public TokenServiceImpl(AuthenticationProxy authenticationProxy) {
        this.authenticationProxy = authenticationProxy;
    }

    @Override
    public ResponseEntity<UserDto> callValidateToken(String token) {
        try {
            return authenticationProxy.validateToken(token);
        } catch (InvalidOrExpiredTokenException exception) {
            throw new InvalidOrExpiredTokenException(exception.getMessage());
        }
    }
}
