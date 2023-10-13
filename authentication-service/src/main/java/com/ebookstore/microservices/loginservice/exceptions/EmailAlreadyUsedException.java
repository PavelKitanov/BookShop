package com.ebookstore.microservices.loginservice.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {


    public EmailAlreadyUsedException(String message){
        super(message);
    }

}
