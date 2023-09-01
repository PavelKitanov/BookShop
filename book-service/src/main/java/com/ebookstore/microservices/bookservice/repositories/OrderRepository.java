package com.ebookstore.microservices.bookservice.repositories;

import com.ebookstore.microservices.bookservice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findOrderByCustomerId(Long customerId);
}
