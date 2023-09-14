package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import com.ebookstore.microservices.bookservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private final AuthenticationProxy authenticationProxy;

    @Autowired
    private final CartService cartService;

    public CartController(AuthenticationProxy authenticationProxy, CartService cartService) {
        this.authenticationProxy = authenticationProxy;
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

    @PostMapping("/createCart")
    public ResponseEntity<?> createCart(@RequestHeader("Authorization") String tokenHeader){

        ResponseEntity<String> response = authenticationProxy.validateToken(tokenHeader);

        if(response.getStatusCode().is2xxSuccessful()){
            Long customerId = Long.parseLong(response.getBody());
            return ResponseEntity.ok(cartService.create(new Cart(customerId)));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
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
