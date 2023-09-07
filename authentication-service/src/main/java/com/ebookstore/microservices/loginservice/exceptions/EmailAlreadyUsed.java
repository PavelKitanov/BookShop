package com.ebookstore.microservices.loginservice.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyUsed extends RuntimeException {

    private HttpStatus status;

    public EmailAlreadyUsed(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return this.status;
    }
}
