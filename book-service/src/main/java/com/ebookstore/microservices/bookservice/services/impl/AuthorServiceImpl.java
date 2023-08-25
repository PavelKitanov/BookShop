package com.ebookstore.microservices.bookservice.services.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ebookstore.microservices.bookservice.exceptions.AuthorNotFoundException;
import com.ebookstore.microservices.bookservice.models.Author;
import com.ebookstore.microservices.bookservice.repositories.AuthorRepository;
import com.ebookstore.microservices.bookservice.services.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {
	private final AuthorRepository authorRepository;

	public AuthorServiceImpl(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	@Override
	public List<Author> findAll() {
		return authorRepository.findAll();
	}

	@Override
	public Author findById(Long id) {
		return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " is not found.",HttpStatus.NOT_FOUND));
	}

	@Override
	public Author save(Author author) {
		return authorRepository.save(author);
	}

	@Override
	public void deleteById(Long id) {
		authorRepository.deleteById(id);
	}

	@Override
	public Author findByFirstNameAndLastName(String firstName, String lastName) {
		return authorRepository.findByFirstNameAndLastName(firstName, lastName);
	}

	@Override
	public Author update(Long id, String firstName, String lastName) {
		Author author = findById(id);
		author.setFirstName(firstName);
		author.setLastName(lastName);
		return authorRepository.save(author);
	}
	
	
	
}
