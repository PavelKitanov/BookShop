package com.ebookstore.microservices.bookservice.services.impl;

import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.repositories.BookRepository;
import com.ebookstore.microservices.bookservice.repositories.CartItemRepository;
import com.ebookstore.microservices.bookservice.services.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, BookRepository bookRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(null);
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem save(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId).orElseThrow(null);
        CartItem cartItem = cartItemRepository.findCartItemByBook(book);
        if(cartItem == null)
            return cartItemRepository.save(new CartItem(book, quantity));
        else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepository.save(cartItem);
        }

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
}
