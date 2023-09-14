package com.ebookstore.microservices.loginservice.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyUsedException extends RuntimeException {


    public EmailAlreadyUsedException(String message){
        super(message);
    }

}
