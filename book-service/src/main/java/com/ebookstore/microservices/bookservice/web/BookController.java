package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.services.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;
	
	public BookController(BookService bookService) {
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
	
	@PostMapping
	public Book addBook(@RequestParam(required = false) Long id,
						@RequestParam String title,
						@RequestParam(required = false) Long authorId,
						@RequestParam String firstName,
						@RequestParam String lastName,
						@RequestParam Long genreId,
						@RequestParam String description,
						@RequestParam double price) {
		if(id == null) 
			return bookService.save(title, authorId, firstName, lastName, genreId, description, price);
		else {
			return bookService.update(id, title, authorId, firstName, lastName, genreId, description, price);
		}
			
	}
	
	@DeleteMapping("/{id}/delete")
	public void removeBook(@PathVariable Long id) {
		bookService.deleteById(id);
	}
	
}
