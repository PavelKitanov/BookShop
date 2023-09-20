package com.ebookstore.microservices.bookservice.exceptions;

public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(String message){
        super(message);
    }
}
