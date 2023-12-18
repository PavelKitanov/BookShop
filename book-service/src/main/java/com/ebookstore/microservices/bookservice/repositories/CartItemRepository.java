package com.ebookstore.microservices.bookservice.repositories;

import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findCartItemByBook(Book book);

    @Query("SELECT i from CartItem i WHERE i.book = ?1 AND i.customerId = ?2 AND i.isOrdered = false")
    CartItem findCartItemByBookAndCustomerId(Book book, Long customerId);

}
