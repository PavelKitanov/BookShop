package com.ebookstore.microservices.bookservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ebookstore.microservices.bookservice.exceptions.AuthorNotFoundException;
import com.ebookstore.microservices.bookservice.exceptions.BookNotFoundException;
import com.ebookstore.microservices.bookservice.models.Author;
import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.repositories.AuthorRepository;
import com.ebookstore.microservices.bookservice.repositories.BookRepository;
import com.ebookstore.microservices.bookservice.services.BookService;

@Service
public class BookServiceImpl implements BookService {
	
	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;
	
	public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
	}

	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	@Override
	public Book findById(Long id) {
		return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book with id " + id + " is not found.",HttpStatus.NOT_FOUND));
	}
	
	@Override
	public List<Book> findByAuthor(Long id) {
		Author author = authorRepository.findById(id).orElseThrow(() -> new  AuthorNotFoundException("Author with id " + id + " is not found.",HttpStatus.NOT_FOUND));
		return bookRepository.findAll().stream().filter(b -> b.getAuthor().equals(author)).collect(Collectors.toList());
	}

	@Override
	public Book save(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public void deleteById(Long id) {
		bookRepository.deleteById(id);
	}

}
