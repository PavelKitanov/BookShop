package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.exceptions.CartItemNotFoundException;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import com.ebookstore.microservices.bookservice.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartItems")
public class CartItemController {

    @Autowired
    private final AuthenticationProxy authenticationProxy;
    private final CartItemService cartItemService;

    public CartItemController(AuthenticationProxy authenticationProxy, CartItemService cartItemService) {
        this.authenticationProxy = authenticationProxy;
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getAll(@RequestHeader("Authorization") String tokenHeader){
        authenticationProxy.validateToken(tokenHeader);
        return ResponseEntity.ok(cartItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCartItemById(@RequestHeader("Authorization") String tokenHeader,
                                    @PathVariable Long id){
        authenticationProxy.validateToken(tokenHeader);

        try{
            CartItem cartItem = cartItemService.findById(id);
            return ResponseEntity.ok(cartItem);
        }
        catch (CartItemNotFoundException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PostMapping("/updateCartItem/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestHeader("Authorization") String tokenHeader,@PathVariable Long cartItemId, @RequestParam int quantity){
        authenticationProxy.validateToken(tokenHeader);

        return ResponseEntity.ok(cartItemService.update(cartItemId, quantity));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@RequestHeader("Authorization") String tokenHeader,
                               @PathVariable Long id){
        authenticationProxy.validateToken(tokenHeader);
        cartItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
