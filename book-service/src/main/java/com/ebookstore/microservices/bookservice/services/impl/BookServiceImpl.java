package com.ebookstore.microservices.bookservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ebookstore.microservices.bookservice.exceptions.AuthorNotFoundException;
import com.ebookstore.microservices.bookservice.exceptions.BookNotFoundException;
import com.ebookstore.microservices.bookservice.exceptions.GenreNotFoundException;
import com.ebookstore.microservices.bookservice.models.Author;
import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.Genre;
import com.ebookstore.microservices.bookservice.repositories.AuthorRepository;
import com.ebookstore.microservices.bookservice.repositories.BookRepository;
import com.ebookstore.microservices.bookservice.repositories.GenreRepository;
import com.ebookstore.microservices.bookservice.services.BookService;

@Service
public class BookServiceImpl implements BookService {
	
	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;
	private final GenreRepository genreRepository;
	
	public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
		this.genreRepository = genreRepository;
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

	@Override
	public Book update(Long bookId, String title, Long authorId, String firstName, String lastName, Long genreId, String description, double price) {
		Author author;
		if(authorId != null)
			author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException("Author with id " + authorId + " is not found",HttpStatus.NOT_FOUND));
		else {
			author = authorRepository.findByFirstNameAndLastName(firstName, lastName);
			if(author == null) {
				author = new Author(firstName, lastName);
				authorRepository.save(author);
			}
		}
		Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " is not found",HttpStatus.NOT_FOUND));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " is not found.",HttpStatus.NOT_FOUND));
		book.setTitle(title);
		book.setAuthor(author);
		book.setGenre(genre);
		book.setDescription(description);
		book.setPrice(price);
		bookRepository.save(book);
		return book;
	}

	@Override
	public Book save(String title, Long authorId, String firstName, String lastName, Long genreId, String description, double price) {
		Author author;
		if(authorId != null)
			author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException("Author with id " + authorId + " is not found",HttpStatus.NOT_FOUND));
		else {
			author = authorRepository.findByFirstNameAndLastName(firstName, lastName);
			if(author == null) {
				author = new Author(firstName, lastName);
				authorRepository.save(author);
			}
		}
		Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new GenreNotFoundException("Genre with id " + genreId + " is not found",HttpStatus.NOT_FOUND));
		Book book = new Book(title, author, genre, description, price);
		author.addBook(book);
		bookRepository.save(book);
		return book;
	}

}
