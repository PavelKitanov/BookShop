package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebookstore.microservices.bookservice.models.Author;
import com.ebookstore.microservices.bookservice.services.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	private final AuthorService authorService;
	
	public AuthorController(AuthorService authorService) {
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
	public Author createAuthor(@RequestBody Author author) {
		if(author.getId() == null)
			return authorService.save(author);
		else
			return authorService.update(author.getId(), author.getFirstName(), author.getLastName());
	}
	
	@DeleteMapping("/{id}/delete")
	public void removeAuthor(@PathVariable Long id) {
		authorService.deleteById(id);
	}
	
	
}
