package com.ebookstore.microservices.bookservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Genre {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long genreId;
	
	private String genre;
	
	public Genre() {
		
	}
	
	public Genre(String genre) {
		this.genre = genre;
	}

	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long id) {
		this.genreId = id;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	
}
