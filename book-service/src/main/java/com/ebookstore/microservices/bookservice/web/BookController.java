package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebookstore.microservices.bookservice.models.Author;
import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.services.AuthorService;
import com.ebookstore.microservices.bookservice.services.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;
	private final AuthorService authorService;
	
	public BookController(BookService bookService, AuthorService authorService) {
		this.bookService = bookService;
		this.authorService = authorService;
	}
	
	@GetMapping
	public List<Book> getAll(){
		return bookService.findAll();
	}
	
	@GetMapping("/{id}")
	public Book getBookById(@PathVariable Long id) {
		return bookService.findById(id);
	}
	
	@GetMapping("/author/{id}")
	public List<Book> getBooksByAuthor(@PathVariable Long id){
		return bookService.findByAuthor(id);
	}
	
	@PostMapping
	public Book addBook(@RequestParam String title,
						@RequestParam String firstName,
						@RequestParam String lastName) {
		Author author = authorService.findByFirstNameAndLastName(firstName, lastName);
		if(author == null) {
			author = new Author(firstName, lastName);
		}
		Book book = new Book(title, author);
		author.addBook(book);
		authorService.save(author);
		return bookService.save(book);
	}
	
	@DeleteMapping("/{id}/delete")
	public void removeBook(@PathVariable Long id) {
		bookService.deleteById(id);
	}
	
}
