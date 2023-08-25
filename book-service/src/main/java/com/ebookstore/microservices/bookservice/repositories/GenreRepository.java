package com.ebookstore.microservices.bookservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebookstore.microservices.bookservice.models.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
	
}
