package com.ebookstore.microservices.bookservice.services;

import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.models.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAll();
    Order findById(Long id);
    Order findByCustomerId(Long customerId);
    Order create(Order order);
    Order create(Long customerId, Long cartId, Discount discount);

    public double calculateDiscountOnTotalPrice(double totalPrice, Discount discount);
    void deleteById(Long id);

}
