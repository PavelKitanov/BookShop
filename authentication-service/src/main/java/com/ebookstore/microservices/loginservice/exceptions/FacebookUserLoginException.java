package com.ebookstore.microservices.loginservice.exceptions;

public class FacebookUserLoginException extends RuntimeException{

    public FacebookUserLoginException(String message){
        super(message);
    }
}
