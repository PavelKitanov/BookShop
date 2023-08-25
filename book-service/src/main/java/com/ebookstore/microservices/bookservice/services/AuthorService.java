package com.ebookstore.microservices.bookservice.services;

import java.util.List;

import com.ebookstore.microservices.bookservice.models.Author;

public interface AuthorService {

	List<Author> findAll();
	Author findById(Long id);
	Author save(Author author);
	Author update(Long id, String firstName, String lastName);
	void deleteById(Long id);
	Author findByFirstNameAndLastName(String firstName, String lastName);
}
