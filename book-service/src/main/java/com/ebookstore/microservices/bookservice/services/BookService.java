package com.ebookstore.microservices.bookservice.services;

import java.util.List;

import com.ebookstore.microservices.bookservice.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

	List<Book> findAll();
	Book findById(Long id);
	List<Book> findByAuthor(Long authorId);
	Book save(Book book);
	Book save(String title, Long authorId, String firstName, String lastName, Long genreId, String description, double price, String imageURL);
	Book update(Long bookId, String title, Long authorId, String firstName, String lastName, Long genreId, String description, double price, String imageURL);
	void deleteById(Long id);
	Book rateBook(Long bookId, int rating, Long customerId);
	List<Book> findAllBooksByTitleContaining(String title);
	List<Book> findByGenre(Long genreId);

}

