package com.ebookstore.microservices.bookservice.services.impl;

import com.ebookstore.microservices.bookservice.exceptions.CartNotFoundException;
import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.repositories.BookRepository;
import com.ebookstore.microservices.bookservice.repositories.CartItemRepository;
import com.ebookstore.microservices.bookservice.services.BookService;
import com.ebookstore.microservices.bookservice.services.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new CartNotFoundException("Cart Item with id " + id + " is not found"));
    }

    @Override
    public CartItem save(CartItem cartItem) {
        Book book = cartItem.getBook();
        Long customerId = cartItem.getCustomerId();
        CartItem cartItemIsPresent = cartItemRepository.findCartItemByBookAndCustomerId(book, customerId);
        if(cartItemIsPresent != null) {
            cartItemIsPresent.setQuantity(cartItemIsPresent.getQuantity() + cartItem.getQuantity());
            return cartItemRepository.save(cartItemIsPresent);
        }
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem update(Long id, int quantity) {
        CartItem cartItem = findById(id);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }
}
