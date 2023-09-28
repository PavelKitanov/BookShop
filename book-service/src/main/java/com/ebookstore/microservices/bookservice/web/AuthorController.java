package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import com.ebookstore.microservices.bookservice.exceptions.AuthorNotFoundException;
import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ebookstore.microservices.bookservice.models.Author;
import com.ebookstore.microservices.bookservice.services.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	@Autowired
	private final AuthenticationProxy authenticationProxy;
	private final AuthorService authorService;
	
	public AuthorController(AuthenticationProxy authenticationProxy, AuthorService authorService) {
		this.authenticationProxy = authenticationProxy;
		this.authorService = authorService;
	}
	
	@GetMapping
	public ResponseEntity<List<Author>> getAll(){
		return ResponseEntity.ok(authorService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
		try {
			Author author = authorService.findById(id);
			return ResponseEntity.ok(author);
		}
		catch (AuthorNotFoundException exception){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<Author> createAuthor(@RequestHeader("Authorization") String tokenHeader,
							   @RequestBody Author author) {
		authenticationProxy.validateToken(tokenHeader);
		if(author.getAuthorId() == null)
			return ResponseEntity.ok(authorService.save(author));
		else
			return ResponseEntity.ok(authorService.update(author.getAuthorId(), author.getFirstName(), author.getLastName()));
	}
	
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<Void> removeAuthor(@RequestHeader("Authorization") String tokenHeader,
							 @PathVariable Long id) {
		authenticationProxy.validateToken(tokenHeader);
		authorService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
