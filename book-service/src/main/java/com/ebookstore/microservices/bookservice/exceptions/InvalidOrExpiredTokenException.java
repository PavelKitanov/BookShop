package com.ebookstore.microservices.bookservice.exceptions;

public class InvalidOrExpiredTokenException extends RuntimeException{

    public InvalidOrExpiredTokenException(String message){
        super(message);
    }
}
