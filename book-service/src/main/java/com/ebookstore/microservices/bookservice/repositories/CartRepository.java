package com.ebookstore.microservices.bookservice.repositories;

import com.ebookstore.microservices.bookservice.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.customerId = ?1 AND c.isActive = true")
    Optional<Cart> getActiveCartByCustomerId(Long customerId);
}
