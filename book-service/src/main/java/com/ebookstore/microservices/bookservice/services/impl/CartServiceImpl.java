package com.ebookstore.microservices.bookservice.services.impl;

import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.exceptions.CartNotFoundException;
import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.repositories.CartRepository;
import com.ebookstore.microservices.bookservice.services.BookService;
import com.ebookstore.microservices.bookservice.services.CartItemService;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.services.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        return cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("The cart with id " + id + "is not found"));
    }

    @Override
    public Cart getCartByCustomerId(Long customerId) {
        return cartRepository.getActiveCartByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException("Cart for the customer with id " + customerId +" is not found."));
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart create(Cart cart) {
        Cart existingCart = cartRepository.getActiveCartByCustomerId(cart.getCustomerId()).orElse(null);

        if(existingCart != null){
            existingCart.setActive(false);
            cartRepository.save(existingCart);
        }

        Cart createdCart = cartRepository.save(cart);
        return createdCart;
    }

    @Override
    public Cart addItemToCart(Long customerId, Long bookId, int quantity) {
        Cart cart = cartRepository.getActiveCartByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException("Cart for the customer with id " + customerId +" is not found."));
        Book book = bookService.findById(bookId);

        CartItem cartItem = cartItemService.save(new CartItem(book, quantity, customerId));
        if(cart.getCartItems().stream().noneMatch(c -> c.getCartItemId().equals(cartItem.getCartItemId()))) {
            cart.addItemToCart(cartItem);
            book.getCartItems().add(cartItem);
        }


        cart.updateTotalPrice();
        bookService.save(book);
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItemFromCart(Long customerId, Long itemId) {
        Cart cart = cartRepository.getActiveCartByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException("Cart for the customer with id " + customerId +" is not found."));
        CartItem cartItem = cartItemService.findById(itemId);

        if(cartItem != null){
            cart.removeItemFromCart(cartItem);
            cartItemService.delete(cartItem);
            cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public Cart removeAllItemsFromCart(Long customerId) {
        Cart cart = cartRepository.getActiveCartByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException("Cart for the customer with id " + customerId +" is not found."));


        cart.setCartTotalPrice(0);
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem item : cartItems)
            cartItemService.deleteById(item.getCartItemId());
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public double couponCode(Long userId, String discount) {
        Cart cart = getCartByCustomerId(userId);
        double totalPrice = cart.getCartTotalPrice();
        if(Objects.equals(discount, Discount.LOW.toString()))
            return ((totalPrice / 100) * 10);
        else if(Objects.equals(discount, Discount.MEDIUM.toString()))
            return ((totalPrice / 100) * 30);
        else if(Objects.equals(discount, Discount.HIGH.toString()))
            return ((totalPrice / 100) * 50);
        else
            return 0;
    }

    @Override
    public CartItem update(Long cartItemId, int quantity, Long customerId) {
        CartItem cartItem = cartItemService.findById(cartItemId);
        cartItem.setQuantity(quantity);

        Cart cart = this.getCartByCustomerId(customerId);
        cart.updateTotalPrice();
        cartRepository.save(cart);

        return cartItemService.update(cartItem);
    }
}
