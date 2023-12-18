package com.ebookstore.microservices.bookservice.services;


import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.CartItem;

import java.util.List;

public interface CartService {

    List<Cart> findAll();
    Cart getCartById(Long id);
    Cart getCartByCustomerId(Long id);
    Cart save(Cart cart);
    Cart create(Cart cart);
    Cart addItemToCart(Long customerId, Long bookId, int quantity);
    Cart removeItemFromCart(Long customerId, Long itemId);
    Cart removeAllItemsFromCart(Long customerId);
    void deleteById(Long id);
    double couponCode(Long userId, String discount);
    CartItem update(Long cartItemId, int quantity, Long customerId);
}
