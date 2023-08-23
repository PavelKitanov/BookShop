package com.ebookstore.microservices.bookservice.exceptions;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends RuntimeException {

	private final HttpStatus status;
	
	public BookNotFoundException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
}
