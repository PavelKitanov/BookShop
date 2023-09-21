package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import com.ebookstore.microservices.bookservice.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<CartItem> getAll(@RequestHeader("Authorization") String tokenHeader){
        authenticationProxy.validateToken(tokenHeader);
        return cartItemService.findAll();
    }

    @GetMapping("/{id}")
    public CartItem getCartItemById(@RequestHeader("Authorization") String tokenHeader,
                                    @PathVariable Long id){
        authenticationProxy.validateToken(tokenHeader);
        return cartItemService.findById(id);
    }

    @PostMapping("/updateCartItem/{cartItemId}")
    public CartItem updateCartItem(@RequestHeader("Authorization") String tokenHeader,@PathVariable Long cartItemId, @RequestParam int quantity){
        authenticationProxy.validateToken(tokenHeader);

        return cartItemService.update(cartItemId, quantity);
    }


    @DeleteMapping("/{id}")
    public void removeCartItem(@RequestHeader("Authorization") String tokenHeader,
                               @PathVariable Long id){
        authenticationProxy.validateToken(tokenHeader);
        cartItemService.deleteById(id);
    }

}
