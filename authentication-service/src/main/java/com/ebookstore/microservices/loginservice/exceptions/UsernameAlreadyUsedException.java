package com.ebookstore.microservices.loginservice.exceptions;

public class UsernameAlreadyUsedException extends RuntimeException{

    public UsernameAlreadyUsedException(String message){
        super(message);
    }
}
