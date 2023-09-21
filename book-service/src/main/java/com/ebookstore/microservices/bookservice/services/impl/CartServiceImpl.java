package com.ebookstore.microservices.bookservice.services.impl;

import com.ebookstore.microservices.bookservice.exceptions.CartNotFoundException;
import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.repositories.BookRepository;
import com.ebookstore.microservices.bookservice.repositories.CartItemRepository;
import com.ebookstore.microservices.bookservice.repositories.CartRepository;
import com.ebookstore.microservices.bookservice.services.BookService;
import com.ebookstore.microservices.bookservice.services.CartItemService;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.exceptions.CartItemNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BookService bookService;
    private final CartItemService cartItemService;

    public CartServiceImpl(CartRepository cartRepository, BookService bookService, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.bookService = bookService;
        this.cartItemService = cartItemService;
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException("The cart with id " + id + "is not found"));
    }

    @Override
    public Cart getCartByCustomerId(Long customerId) {
        return cartRepository.getCartByCustomerId(customerId).orElseThrow(() -> new CartNotFoundException("Cart for the customer with id " + customerId +" is not found."));
    }

    @Override
    public Cart create(Cart cart) {
        Cart createdCart =cartRepository.save(cart);
        return createdCart;
    }

    @Override
    public Cart addItemToCart(Long customerId, Long bookId, int quantity) {
        Cart cart = cartRepository.getCartByCustomerId(customerId).orElseThrow(() -> new CartNotFoundException("Cart for the customer with id " + customerId +" is not found."));
        Book book = bookService.findById(bookId);

        CartItem cartItem = cartItemService.save(new CartItem(book, quantity, customerId));
        if(cart.getCartItems().stream().noneMatch(c -> c.getCartItemId().equals(cartItem.getCartItemId())))
            cart.addItemToCart(cartItem);

        cart.updateTotalPrice();
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItemFromCart(Long customerId, Long itemId) {
        Cart cart = cartRepository.getCartByCustomerId(customerId).orElseThrow(() -> new CartNotFoundException("Cart for the customer with id " + customerId +" is not found."));;
        CartItem cartItem = cartItemService.findById(itemId);

        if(cartItem != null){
            cart.removeItemFromCart(cartItem);
            cartItemService.delete(cartItem);
            cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }
}
