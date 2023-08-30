package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.services.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/{id}")
    public Cart getCartById(@PathVariable Long id){
        return cartService.getCartById(id);
    }

    @GetMapping("/cartByCustomer/{customerId}")
    public Cart getCartByCustomerId(@PathVariable Long customerId){
        return cartService.getCartByCustomerId(customerId);
    }

    @PostMapping("/createCart/{customerId}")
    public Cart createCart(@PathVariable Long customerId){
        return cartService.create(new Cart(customerId));
    }

    @PostMapping("/addItemToCart")
    public Cart addItemToCart(@RequestParam Long customerId, @RequestParam Long bookId, @RequestParam int quantity){
        return cartService.addItemToCart(customerId, bookId, quantity);
    }

    @DeleteMapping("/cart/{id}")
    public void deleteCart(@PathVariable Long id){
        cartService.deleteById(id);
    }

    @DeleteMapping("/removeItemFromCart/{cartItemId}")
    public Cart removeItemFromCart(@PathVariable Long cartItemId, @RequestParam Long customerId){
        return cartService.removeItemFromCart(customerId, cartItemId);
    }

}
