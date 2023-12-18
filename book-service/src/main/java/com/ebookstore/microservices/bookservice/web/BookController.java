package com.ebookstore.microservices.bookservice.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ebookstore.microservices.bookservice.dto.BookDto;
import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.models.Order;
import com.ebookstore.microservices.bookservice.models.Rating;
import com.ebookstore.microservices.bookservice.payload.ItemBasedRecommendationRequest;
import com.ebookstore.microservices.bookservice.proxy.RecommendationProxy;
import com.ebookstore.microservices.bookservice.services.OrderService;
import com.ebookstore.microservices.bookservice.services.RatingService;
import com.ebookstore.microservices.bookservice.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.services.BookService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private final TokenService tokenService;

	@Autowired
	private final RecommendationProxy recommendationProxy;

	@Autowired
	private final BookService bookService;

	@Autowired
	private final OrderService orderService;

	@Autowired
	private final RatingService ratingService;
	
	public BookController(TokenService tokenService, RecommendationProxy recommendationProxy, BookService bookService, OrderService orderService, RatingService ratingService) {
		this.tokenService = tokenService;
		this.recommendationProxy = recommendationProxy;
		this.bookService = bookService;
		this.orderService = orderService;
		this.ratingService = ratingService;
	}

	@GetMapping
	public ResponseEntity<List<Book>> getAll(@RequestHeader("Authorization") String tokenHeader,
											 @RequestParam(required = false) String title,
											 @RequestParam(required = false) Long authorId,
											 @RequestParam(required = false) Long genreId){
		tokenService.callValidateToken(tokenHeader);
		List<Book> books;
		if(title == null && authorId == null && genreId == null)
			books = bookService.findAll();
		else if(authorId == null && genreId == null)
			books = bookService.findAllBooksByTitleContaining(title);
		else if(title == null && genreId == null)
			books = bookService.findByAuthor(authorId);
		else if(title == null && authorId == null)
			books = bookService.findByGenre(genreId);
		else if(genreId == null)
			books = bookService.findAllBooksByTitleContaining(title)
					.stream().filter(book -> book.getAuthor().getAuthorId().equals(authorId))
					.toList();
		else if(authorId == null)
			books = bookService.findAllBooksByTitleContaining(title)
					.stream().filter(book -> book.getGenre().getGenreId().equals(genreId)).toList();
		else if(title == null)
			books = bookService.findByAuthor(authorId)
					.stream().filter(book -> book.getGenre().getGenreId().equals(genreId))
					.toList();
		else
			books = bookService.findAllBooksByTitleContaining(title)
					.stream().filter(book -> book.getGenre().getGenreId().equals(genreId) &&
							book.getAuthor().getAuthorId().equals(authorId))
					.toList();

		return ResponseEntity.ok(books);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@RequestHeader("Authorization") String tokenHeader,
											@PathVariable Long id) {
		tokenService.callValidateToken(tokenHeader);
        Book book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }
	
	@GetMapping("/author/{authorId}")
	public ResponseEntity<List<Book>> getBooksByAuthor(@RequestHeader("Authorization") String tokenHeader,
													   @PathVariable Long authorId){
		tokenService.callValidateToken(tokenHeader);
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
										@RequestParam double price,
										@RequestParam String imageURL) {
		tokenService.callValidateToken(tokenHeader);
		if(id == null) 
			return ResponseEntity.ok(bookService.save(title, authorId, firstName, lastName, genreId, description, price, imageURL));
		else {
			return ResponseEntity.ok(bookService.update(id, title, authorId, firstName, lastName, genreId, description, price, imageURL));
		}
			
	}

	@PostMapping("/rate")
	public ResponseEntity<Book> rateBook(@RequestHeader("Authorization") String tokenHeader,
										 @RequestParam Long bookId,
										 @RequestParam int rating){
		ResponseEntity<UserDto> response = tokenService.callValidateToken(tokenHeader);
		UserDto userDto = response.getBody();

		Book book = bookService.rateBook(bookId, rating, userDto.getUserId());

		return ResponseEntity.ok(book);
	}

	@DeleteMapping("/unrate")
	public ResponseEntity<Void> unrateBook(@RequestHeader("Authorization") String tokenHeader,
										   @RequestParam Long ratingId){
		tokenService.callValidateToken(tokenHeader);
		ratingService.deleteById(ratingId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/recommendations")
	public ResponseEntity<List<Book>> getBookRecommendations(@RequestHeader("Authorization") String tokenHeader){
		ResponseEntity<UserDto> authResponse = tokenService.callValidateToken(tokenHeader);
		UserDto userDto = authResponse.getBody();

		List<Book> recommendedBooks;

		List<BookDto> allBooks = new ArrayList<>();
		for(Book book : bookService.findAll()){
			allBooks.add(new BookDto(book.getBookId(),
					book.getTitle(),
					book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName(),
					book.getGenre().getGenre(),
					book.getRatings().stream().mapToInt(Rating::getRating).boxed().toList()));
		}

		List<Order> customerOrders = orderService.findOrdersByCustomerId(userDto.getUserId());
		if(!customerOrders.isEmpty()) {
			Order order = customerOrders.get(customerOrders.size()-1);
			List<CartItem> cartItems = order.getCart().getCartItems();
			List<BookDto> customerBooks = new ArrayList<>();
			for (CartItem item : cartItems) {
				customerBooks.add(new BookDto(item.getBook().getBookId(),
						item.getBook().getTitle(),
						item.getBook().getAuthor().getFirstName() + " " + item.getBook().getAuthor().getLastName(),
						item.getBook().getGenre().getGenre(),
						item.getBook().getRatings().stream().mapToInt(Rating::getRating).boxed().toList()));
			}

			ResponseEntity<List<BookDto>> response = recommendationProxy.itemBasedRecommenderSystem(new ItemBasedRecommendationRequest(allBooks, customerBooks));
			List<BookDto> recommendedBooksDto = response.getBody();
			recommendedBooks = recommendedBooksDto.stream().map(bookDto -> bookService.findById(bookDto.getBookId())).toList();
		}
		else {
			ResponseEntity<List<BookDto>> response = recommendationProxy.popularityBasedRecommenderSystem(allBooks);
			List<BookDto> recommendedBooksDto = response.getBody();

			recommendedBooks = recommendedBooksDto.stream().map(bookDto -> bookService.findById(bookDto.getBookId())).toList();
		}

		return ResponseEntity.ok(recommendedBooks);
	}
	
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<Void> removeBook(@RequestHeader("Authorization") String tokenHeader,
						   @PathVariable Long id) {
		tokenService.callValidateToken(tokenHeader);
		bookService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}
