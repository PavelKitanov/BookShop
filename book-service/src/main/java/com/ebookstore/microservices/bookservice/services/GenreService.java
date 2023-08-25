package com.ebookstore.microservices.bookservice.services;

import java.util.List;

import com.ebookstore.microservices.bookservice.models.Genre;

public interface GenreService {
	
	List<Genre> findAll();
	Genre findById(Long id);
	Genre save(Genre genre);
	Genre update(Long id, String genre);
	void deleteById(Long id);
}
