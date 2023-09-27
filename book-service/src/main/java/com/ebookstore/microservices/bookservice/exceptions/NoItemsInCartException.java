package com.ebookstore.microservices.bookservice.exceptions;

public class NoItemsInCartException extends RuntimeException{

    public NoItemsInCartException(String message){
        super(message);
    }
}
