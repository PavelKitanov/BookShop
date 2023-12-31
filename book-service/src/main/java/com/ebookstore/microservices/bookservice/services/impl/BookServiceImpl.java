package com.ebookstore.microservices.bookservice.services.impl;

import java.util.List;

import com.ebookstore.microservices.bookservice.models.*;
import com.ebookstore.microservices.bookservice.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ebookstore.microservices.bookservice.exceptions.BookNotFoundException;
import com.ebookstore.microservices.bookservice.repositories.BookRepository;

@Service
public class BookServiceImpl implements BookService {
	
	private final BookRepository bookRepository;
	private final AuthorService authorService;
	private final GenreService genreService;
	private final CartItemService cartItemService;
	private final RatingService ratingService;

	public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, GenreService genreService, CartItemService cartItemService, RatingService ratingService) {
		this.bookRepository = bookRepository;
		this.authorService = authorService;
		this.genreService = genreService;
		this.cartItemService = cartItemService;
		this.ratingService = ratingService;
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
	public List<Book> findByAuthor(Long authorId) {
		Author author = authorService.findById(authorId);
		return bookRepository.findAll().stream().filter(b -> b.getAuthor().equals(author)).toList();
	}
	@Override
	public Book save(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public void deleteById(Long id) {
		Book book = findById(id);
		book.getRatings().clear();
		for(CartItem cartItem : book.getCartItems()){
			cartItem.setBook(null);
			cartItemService.save(cartItem);
		}
		bookRepository.deleteById(id);
	}

	@Override
	public Book rateBook(Long bookId, int rating, Long customerId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " is not found.",HttpStatus.NOT_FOUND));

		Rating existingRating = ratingService.findRatingByBookAndCustomerId(book, customerId);

		if(existingRating != null){
			existingRating.setRating(rating);
			ratingService.save(existingRating);
		}
		else{
			Rating bookRating = ratingService.save(new Rating(rating, customerId, book));
			book.getRatings().add(bookRating);
			bookRepository.save(book);
		}
		return book;
	}

	@Override
	public List<Book> findAllBooksByTitleContaining(String title) {
		return this.bookRepository.findAllByTitleContaining(title);
	}

	@Override
	public List<Book> findByGenre(Long genreId) {
		Genre genre = genreService.findById(genreId);
		return bookRepository.findAll().stream().filter(book -> book.getGenre().equals(genre)).toList();
	}

	@Override
	public Book update(Long bookId, String title, Long authorId, String firstName, String lastName, Long genreId, String description, double price, String imageURL) {
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
		book.setImageURL(imageURL);
		bookRepository.save(book);
		return book;
	}

	@Override
	public Book save(String title, Long authorId, String firstName, String lastName, Long genreId, String description, double price, String imageURL) {
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
		Book book = new Book(title, author, genre, description, price, imageURL);
		author.addBook(book);
		bookRepository.save(book);
		return book;
	}

}
