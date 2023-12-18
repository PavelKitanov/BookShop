package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.services.CartItemService;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/cartItems")
public class CartItemController {

    @Autowired
    private final TokenService tokenService;
    private final CartItemService cartItemService;
    private final CartService cartService;

    public CartItemController(TokenService tokenService, CartItemService cartItemService, CartService cartService) {
        this.tokenService = tokenService;
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getAll(@RequestHeader("Authorization") String tokenHeader){
        tokenService.callValidateToken(tokenHeader);
        return ResponseEntity.ok(cartItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById(@RequestHeader("Authorization") String tokenHeader,
                                    @PathVariable Long id){
        tokenService.callValidateToken(tokenHeader);

        CartItem cartItem = cartItemService.findById(id);
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping("/updateCartItem/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestHeader("Authorization") String tokenHeader,
                                                   @PathVariable Long cartItemId,
                                                   @RequestParam int quantity){
        ResponseEntity<UserDto> response = tokenService.callValidateToken(tokenHeader);
        UserDto userDto = response.getBody();

        return ResponseEntity.ok(cartService.update(cartItemId, quantity, userDto.getUserId()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@RequestHeader("Authorization") String tokenHeader,
                               @PathVariable Long id){
        tokenService.callValidateToken(tokenHeader);
        cartItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
