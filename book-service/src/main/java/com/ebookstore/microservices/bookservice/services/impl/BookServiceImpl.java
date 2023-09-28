package com.ebookstore.microservices.bookservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.ebookstore.microservices.bookservice.models.*;
import com.ebookstore.microservices.bookservice.services.AuthorService;
import com.ebookstore.microservices.bookservice.services.CartItemService;
import com.ebookstore.microservices.bookservice.services.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ebookstore.microservices.bookservice.exceptions.BookNotFoundException;
import com.ebookstore.microservices.bookservice.repositories.BookRepository;
import com.ebookstore.microservices.bookservice.services.BookService;

@Service
public class BookServiceImpl implements BookService {
	
	private final BookRepository bookRepository;
	private final AuthorService authorService;
	private final GenreService genreService;
	private final CartItemService cartItemService;
	
	public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, GenreService genreService, CartItemService cartItemService) {
		this.bookRepository = bookRepository;
		this.authorService = authorService;
		this.genreService = genreService;
		this.cartItemService = cartItemService;
	}

	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	@Override
	public Book findById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException("Book with id " + id + " is not found.",HttpStatus.NOT_FOUND));
	}
	
	@Override
	public List<Book> findByAuthor(Long id) {
		Author author = authorService.findById(id);
		return bookRepository.findAll().stream().filter(b -> b.getAuthor().equals(author)).collect(Collectors.toList());
	}

	@Override
	public Book save(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public void deleteById(Long id) {
		Book book = findById(id);
		for(CartItem cartItem : book.getCartItems()){
			cartItem.setBook(null);
			cartItemService.save(cartItem);
		}
		bookRepository.deleteById(id);
	}

	@Override
	public Book update(Long bookId, String title, Long authorId, String firstName, String lastName, Long genreId, String description, double price) {
		Author author;
		if(authorId != null)
			author = authorService.findById(authorId);
		else {
			author = authorService.findByFirstNameAndLastName(firstName, lastName);
			if(author == null) {
				author = new Author(firstName, lastName);
				authorService.save(author);
			}
		}
		Genre genre = genreService.findById(genreId);
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " is not found.",HttpStatus.NOT_FOUND));
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
			author = authorService.findById(authorId);
		else {
			author = authorService.findByFirstNameAndLastName(firstName, lastName);
			if(author == null) {
				author = new Author(firstName, lastName);
				authorService.save(author);
			}
		}
		Genre genre = genreService.findById(genreId);
		Book book = new Book(title, author, genre, description, price);
		author.addBook(book);
		bookRepository.save(book);
		return book;
	}

}
