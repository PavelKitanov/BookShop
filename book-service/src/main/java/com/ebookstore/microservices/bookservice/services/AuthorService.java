package com.ebookstore.microservices.bookservice.services;

import java.util.List;
import java.util.Optional;

import com.ebookstore.microservices.bookservice.models.Author;

public interface AuthorService {

	List<Author> findAll();
	Optional<Author> findById(Long id);
	Author save(Author author);
	void deleteById(Long id);
	Author findByFirstNameAndLastName(String firstName, String lastName);
}
