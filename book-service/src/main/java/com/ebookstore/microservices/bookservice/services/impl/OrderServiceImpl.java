package com.ebookstore.microservices.bookservice.services.impl;

import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.Order;
import com.ebookstore.microservices.bookservice.repositories.CartRepository;
import com.ebookstore.microservices.bookservice.repositories.OrderRepository;
import com.ebookstore.microservices.bookservice.services.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }


    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order findByCustomerId(Long customerId) {
        return orderRepository.findOrderByCustomerId(customerId);
    }

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order create(Long customerId, Long cartId, Discount discount) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        Order order = new Order(customerId, cart, discount);
        double totalPrice = cart.getCartTotalPrice();
        if( discount != null)
            totalPrice = calculateDiscountOnTotalPrice(totalPrice, discount);
        order.setOrderTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public double calculateDiscountOnTotalPrice(double totalPrice, Discount discount){
        if(discount == Discount.LOW)
            return totalPrice-((totalPrice / 100) * 10);
        else if(discount == Discount.MEDIUM)
            return totalPrice-((totalPrice / 100) * 30);
        else if(discount == Discount.HIGH)
            return totalPrice-((totalPrice / 100) * 50);
        else
            return totalPrice;
    }
}
