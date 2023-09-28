package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.exceptions.CartNotFoundException;
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
    public ResponseEntity<Cart> getCartById(@RequestHeader("Authorization") String tokenHeader,
                            @PathVariable Long id){

        authenticationProxy.validateToken(tokenHeader);
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping("/cartByCustomer")
    public ResponseEntity<?> getCartByCustomerId(@RequestHeader("Authorization") String tokenHeader){

        ResponseEntity<UserDto> response = authenticationProxy.validateToken(tokenHeader);
        UserDto userDto = response.getBody();

        try {
            Cart cart = cartService.getCartByCustomerId(userDto.getUserId());
            return ResponseEntity.ok(cart);
        }
        catch (CartNotFoundException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PostMapping("/createCart")
    public ResponseEntity<?> createCart(@RequestHeader("Authorization") String tokenHeader){

        try {
            ResponseEntity<UserDto> response = authenticationProxy.validateToken(tokenHeader);
            UserDto userDto = response.getBody();
            return ResponseEntity.ok(cartService.create(new Cart(userDto.getUserId())));
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
        }
    }

    @PostMapping("/addItemToCart")
    public ResponseEntity<Cart> addItemToCart(@RequestHeader("Authorization") String tokenHeader, @RequestParam Long bookId, @RequestParam int quantity){
        ResponseEntity<UserDto> response = authenticationProxy.validateToken(tokenHeader);
        UserDto userDto = response.getBody();
        return ResponseEntity.ok(cartService.addItemToCart(userDto.getUserId(), bookId, quantity));
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<Void> deleteCart(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id){
        authenticationProxy.validateToken(tokenHeader);
        cartService.deleteById(id);
        return  ResponseEntity.noContent().build();
    }

    @DeleteMapping("/removeItemFromCart/{cartItemId}")
    public ResponseEntity<Cart> removeItemFromCart(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long cartItemId){
        ResponseEntity<UserDto> response = authenticationProxy.validateToken(tokenHeader);
        UserDto userDto = response.getBody();

        return ResponseEntity.ok(cartService.removeItemFromCart(userDto.getUserId(), cartItemId));
    }

}
