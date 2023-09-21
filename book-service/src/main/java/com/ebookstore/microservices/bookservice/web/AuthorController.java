package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import org.springframework.beans.factory.annotation.Autowired;
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
	public List<Author> getAll(){
		return authorService.findAll();
	}
	
	@GetMapping("/{id}")
	public Author getAuthorById(@PathVariable Long id) {
		return authorService.findById(id);
	}
	
	@PostMapping
	public Author createAuthor(@RequestHeader("Authorization") String tokenHeader,
							   @RequestBody Author author) {
		authenticationProxy.validateToken(tokenHeader);
		if(author.getAuthorId() == null)
			return authorService.save(author);
		else
			return authorService.update(author.getAuthorId(), author.getFirstName(), author.getLastName());
	}
	
	@DeleteMapping("/{id}/delete")
	public void removeAuthor(@RequestHeader("Authorization") String tokenHeader,
							 @PathVariable Long id) {
		authenticationProxy.validateToken(tokenHeader);
		authorService.deleteById(id);
	}
	
	
}
