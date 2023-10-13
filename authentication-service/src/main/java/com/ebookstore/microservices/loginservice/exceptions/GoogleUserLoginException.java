package com.ebookstore.microservices.loginservice.exceptions;

public class GoogleUserLoginException extends RuntimeException{

    public GoogleUserLoginException(String message){
        super(message);
    }
}
