package com.ebookstore.microservices.bookservice.services;

import com.ebookstore.microservices.bookservice.models.CartItem;

import java.util.List;

public interface CartItemService {

    List<CartItem> findAll();
    CartItem findById(Long id);
    CartItem save(CartItem cartItem);
    CartItem update(Long id, int quantity);

    void deleteById(Long id);

    void delete(CartItem cartItem);
}
