package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import com.ebookstore.microservices.bookservice.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ebookstore.microservices.bookservice.models.Author;
import com.ebookstore.microservices.bookservice.services.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	@Autowired
	private final TokenService tokenService;
	private final AuthorService authorService;
	
	public AuthorController(TokenService tokenService, AuthorService authorService) {
		this.tokenService = tokenService;
		this.authorService = authorService;
	}
	
	@GetMapping
	public ResponseEntity<List<Author>> getAll(){
		return ResponseEntity.ok(authorService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Author author = authorService.findById(id);
        return ResponseEntity.ok(author);
    }
	
	@PostMapping("/add")
	public ResponseEntity<Author> createAuthor(@RequestHeader("Authorization") String tokenHeader,
							   @RequestBody Author author) {
		tokenService.callValidateToken(tokenHeader);
		if(author.getAuthorId() == null)
			return ResponseEntity.ok(authorService.save(author));
		else
			return ResponseEntity.ok(authorService.update(author.getAuthorId(), author.getFirstName(), author.getLastName()));
	}
	
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<Void> removeAuthor(@RequestHeader("Authorization") String tokenHeader,
							 @PathVariable Long id) {
		tokenService.callValidateToken(tokenHeader);
		authorService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
