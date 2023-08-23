package com.ebookstore.microservices.bookservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebookstore.microservices.bookservice.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

}
