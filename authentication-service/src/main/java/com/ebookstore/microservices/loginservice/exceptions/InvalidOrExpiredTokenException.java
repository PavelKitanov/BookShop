package com.ebookstore.microservices.loginservice.exceptions;

public class InvalidOrExpiredTokenException extends RuntimeException{
    public InvalidOrExpiredTokenException(String message){
        super(message);
    }
}
