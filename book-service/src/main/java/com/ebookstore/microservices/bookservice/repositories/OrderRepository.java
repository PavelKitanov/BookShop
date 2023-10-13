package com.ebookstore.microservices.bookservice.repositories;

import com.ebookstore.microservices.bookservice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findOrderByCustomerId(Long customerId);
    List<Order> findOrdersByCustomerId(Long customerId);
}
