package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.services.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private final AuthenticationProxy authenticationProxy;
	private final BookService bookService;
	
	public BookController(AuthenticationProxy authenticationProxy, BookService bookService) {
		this.authenticationProxy = authenticationProxy;
		this.bookService = bookService;
	}
	
	@GetMapping
	public List<Book> getAll(){
		return bookService.findAll();
	}
	
	@GetMapping("/{id}")
	public Book getBookById(@PathVariable Long id) {
		return bookService.findById(id);
	}
	
	@GetMapping("/author/{authorId}")
	public List<Book> getBooksByAuthor(@PathVariable Long authorId){
		return bookService.findByAuthor(authorId);
	}
	
	@PostMapping("/add")
	public Book addBook(@RequestHeader("Authorization") String tokenHeader,
						@RequestParam(required = false) Long id,
						@RequestParam String title,
						@RequestParam(required = false) Long authorId,
						@RequestParam String firstName,
						@RequestParam String lastName,
						@RequestParam Long genreId,
						@RequestParam String description,
						@RequestParam double price) {
		authenticationProxy.validateToken(tokenHeader);
		if(id == null) 
			return bookService.save(title, authorId, firstName, lastName, genreId, description, price);
		else {
			return bookService.update(id, title, authorId, firstName, lastName, genreId, description, price);
		}
			
	}
	
	@DeleteMapping("/{id}/delete")
	public void removeBook(@RequestHeader("Authorization") String tokenHeader,
						   @PathVariable Long id) {
		authenticationProxy.validateToken(tokenHeader);
		bookService.deleteById(id);
	}
	
}
