package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebookstore.microservices.bookservice.models.Genre;
import com.ebookstore.microservices.bookservice.services.GenreService;

@RestController
@RequestMapping("/genres")
public class GenreController {
	
	private final GenreService genreService;

	public GenreController(GenreService genreService) {
		this.genreService = genreService;
	}
	
	@GetMapping
	public List<Genre> getAll(){
		return genreService.findAll();
	}
	
	@PostMapping
	public Genre addGenre(@RequestBody Genre genre) {
		if(genre.getGenreId() == null)
			return genreService.save(genre);
		else	
			return genreService.update(genre.getGenreId(), genre.getGenre());
	}
}
