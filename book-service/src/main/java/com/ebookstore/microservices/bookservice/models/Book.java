package com.ebookstore.microservices.bookservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookId;
	
	private String title;
	
	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genre_id")
	private Genre genre;


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	@JsonIgnore
	private List<CartItem> cartItems;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rating> ratings;
	
	private String description;
	
	private double price;

	private String imageURL;
	
	public Book() {
		
	}
	
	public Book(String title, Author author, Genre genre, String description, double price, String imageURL) {
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.description = description;
		this.price = price;
		this.imageURL = imageURL;
	}
}
