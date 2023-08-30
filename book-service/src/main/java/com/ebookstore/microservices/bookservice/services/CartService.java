package com.ebookstore.microservices.bookservice.services;


import com.ebookstore.microservices.bookservice.models.Cart;

import java.util.List;

public interface CartService {

    List<Cart> findAll();
    Cart getCartById(Long id);
    Cart getCartByCustomerId(Long id);
    Cart create(Cart cart);
    Cart addItemToCart(Long customerId, Long bookId, int quantity);
    Cart removeItemFromCart(Long customerId, Long itemId);
    void deleteById(Long id);
}
