package com.ebookstore.microservices.bookservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebookstore.microservices.bookservice.models.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

	Author findByFirstNameAndLastName(String firstName, String lastName);
}
