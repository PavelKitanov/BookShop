package com.ebookstore.microservices.bookservice.services;

import java.util.List;

import com.ebookstore.microservices.bookservice.models.Book;

public interface BookService {

	List<Book> findAll();
	Book findById(Long id);
	List<Book> findByAuthor(Long id);
	Book save(Book book);
	void deleteById(Long id);
}
