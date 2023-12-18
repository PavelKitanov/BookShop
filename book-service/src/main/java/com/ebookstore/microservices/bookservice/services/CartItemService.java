package com.ebookstore.microservices.bookservice.services;

import com.ebookstore.microservices.bookservice.models.CartItem;

import java.util.List;

public interface CartItemService {

    List<CartItem> findAll();
    CartItem findById(Long id);
    CartItem save(CartItem cartItem);
    void deleteById(Long id);
    CartItem update(CartItem cartItem);
    void delete(CartItem cartItem);
}
