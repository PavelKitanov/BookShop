package com.ebookstore.microservices.bookservice.repositories;

import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findCartItemByBook(Book book);

    CartItem findCartItemByBookAndCustomerId(Book book, Long customerId);
}
