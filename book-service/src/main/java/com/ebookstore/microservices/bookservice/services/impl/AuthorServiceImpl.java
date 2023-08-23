package com.ebookstore.microservices.bookservice.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
	public Optional<Author> findById(Long id) {
		return authorRepository.findById(id);
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
	
	
	
}
