package com.ebookstore.microservices.bookservice.services.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ebookstore.microservices.bookservice.exceptions.GenreNotFoundException;
import com.ebookstore.microservices.bookservice.models.Genre;
import com.ebookstore.microservices.bookservice.repositories.GenreRepository;
import com.ebookstore.microservices.bookservice.services.GenreService;

@Service
public class GenreServiceImpl implements GenreService{

	private final GenreRepository genreRepository;
	
	public GenreServiceImpl(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}
	
	@Override
	public List<Genre> findAll() {
		return genreRepository.findAll();
	}

	@Override
	public Genre findById(Long id) {
		return genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException("Genre with id " + id + " is not found",HttpStatus.NOT_FOUND));
	}

	@Override
	public Genre save(Genre genre) {
		return genreRepository.save(genre);
	}

	@Override
	public void deleteById(Long id) {
		genreRepository.deleteById(id);
	}

	@Override
	public Genre update(Long id, String genre) {
		Genre _genre = findById(id);
		_genre.setGenre(genre);
		return genreRepository.save(_genre);
	}

}
