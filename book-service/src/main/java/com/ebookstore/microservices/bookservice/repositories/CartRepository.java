package com.ebookstore.microservices.bookservice.repositories;

import com.ebookstore.microservices.bookservice.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> getCartByCustomerId(Long customerId);
}
