package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import com.ebookstore.microservices.bookservice.exceptions.BookNotFoundException;
import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Book>> getAll(){
		return ResponseEntity.ok(bookService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getBookById(@PathVariable Long id) {
		try{
			Book book = bookService.findById(id);
			return ResponseEntity.ok(book);
		}
		catch (BookNotFoundException exception){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@GetMapping("/author/{authorId}")
	public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long authorId){
		return ResponseEntity.ok(bookService.findByAuthor(authorId));
	}
	
	@PostMapping("/add")
	public ResponseEntity<Book> addBook(@RequestHeader("Authorization") String tokenHeader,
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
			return ResponseEntity.ok(bookService.save(title, authorId, firstName, lastName, genreId, description, price));
		else {
			return ResponseEntity.ok(bookService.update(id, title, authorId, firstName, lastName, genreId, description, price));
		}
			
	}
	
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<Void> removeBook(@RequestHeader("Authorization") String tokenHeader,
						   @PathVariable Long id) {
		authenticationProxy.validateToken(tokenHeader);
		bookService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
