package com.ebookstore.microservices.bookservice.services;

import java.util.List;

import com.ebookstore.microservices.bookservice.models.Book;


public interface BookService {

	List<Book> findAll();
	Book findById(Long id);
	List<Book> findByAuthor(Long id);
	Book save(Book book);
	Book save(String title, Long authorId, String firstName, String lastName, Long genreId, String description, double price);
	Book update(Long bookId, String title, Long authorId, String firstName, String lastName, Long genreId, String description, double price);
	void deleteById(Long id);
}
