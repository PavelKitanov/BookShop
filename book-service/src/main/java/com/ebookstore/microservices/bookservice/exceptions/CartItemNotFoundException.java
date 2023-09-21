package com.ebookstore.microservices.bookservice.exceptions;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(String message){
        super(message);
    }
}
