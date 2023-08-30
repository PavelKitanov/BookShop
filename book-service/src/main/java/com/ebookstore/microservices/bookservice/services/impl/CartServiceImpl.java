package com.ebookstore.microservices.bookservice.services.impl;

import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.repositories.BookRepository;
import com.ebookstore.microservices.bookservice.repositories.CartItemRepository;
import com.ebookstore.microservices.bookservice.repositories.CartRepository;
import com.ebookstore.microservices.bookservice.services.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository, BookRepository bookRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(null);
    }

    @Override
    public Cart getCartByCustomerId(Long id) {
        return cartRepository.getCartByCustomerId(id);
    }

    @Override
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart addItemToCart(Long customerId, Long bookId, int quantity) {
        Cart cart = cartRepository.getCartByCustomerId(customerId);
        Book book = bookRepository.findById(bookId).orElseThrow(null);

        CartItem cartItem = new CartItem(book, quantity);
        cartItemRepository.save(cartItem);
        cart.addItemToCart(cartItem);

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItemFromCart(Long customerId, Long itemId) {
        Cart cart = cartRepository.getCartByCustomerId(customerId);
        CartItem cartItem = cartItemRepository.findById(itemId).orElse(null);

        if(cartItem != null){
            cart.removeItemFromCart(cartItem);
            cartItemRepository.delete(cartItem);
            cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }
}
