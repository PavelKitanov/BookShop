package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.services.OrderService;
import com.ebookstore.microservices.bookservice.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private final TokenService tokenService;

    @Autowired
    private final CartService cartService;

    public CartController(TokenService tokenService, CartService cartService) {
        this.tokenService = tokenService;
        this.cartService = cartService;
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<Cart> getCartById(@RequestHeader("Authorization") String tokenHeader,
                            @PathVariable Long id){

        tokenService.callValidateToken(tokenHeader);
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping("/cartByCustomer")
    public ResponseEntity<Cart> getCartByCustomerId(@RequestHeader("Authorization") String tokenHeader){

        ResponseEntity<UserDto> response = tokenService.callValidateToken(tokenHeader);
        UserDto userDto = response.getBody();

        Cart cart = cartService.getCartByCustomerId(userDto.getUserId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/createCart")
    public ResponseEntity<Cart> createCart(@RequestHeader("Authorization") String tokenHeader){
        ResponseEntity<UserDto> response = tokenService.callValidateToken(tokenHeader);

        UserDto userDto = response.getBody();
        return ResponseEntity.ok(cartService.create(new Cart(userDto.getUserId())));
    }

    @PostMapping("/addItemToCart")
    public ResponseEntity<Cart> addItemToCart(@RequestHeader("Authorization") String tokenHeader, @RequestParam Long bookId, @RequestParam int quantity){
        ResponseEntity<UserDto> response = tokenService.callValidateToken(tokenHeader);
        UserDto userDto = response.getBody();
        return ResponseEntity.ok(cartService.addItemToCart(userDto.getUserId(), bookId, quantity));
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<Void> deleteCart(@RequestHeader("Authorization") String tokenHeader, @PathVariable Long id){
        tokenService.callValidateToken(tokenHeader);
        cartService.deleteById(id);
        return  ResponseEntity.noContent().build();
    }

    @DeleteMapping("/removeItemFromCart/{cartItemId}")
    public ResponseEntity<Cart> removeItemFromCart(@RequestHeader("Authorization") String tokenHeader,
                                                   @PathVariable Long cartItemId){
        ResponseEntity<UserDto> response = tokenService.callValidateToken(tokenHeader);
        UserDto userDto = response.getBody();

        return ResponseEntity.ok(cartService.removeItemFromCart(userDto.getUserId(), cartItemId));
    }

    @GetMapping("/couponCode")
    public ResponseEntity<Double> couponCode(@RequestHeader("Authorization") String tokenHeader,
                                             @RequestParam String discount){
        ResponseEntity<UserDto> response = tokenService.callValidateToken(tokenHeader);
        UserDto userDto = response.getBody();
        double discountValue = cartService.couponCode(userDto.getUserId(), discount);

        return ResponseEntity.ok(discountValue);
    }

    @DeleteMapping("/removeAllItems")
    public ResponseEntity<Cart> removeAllItems(@RequestHeader("Authorization") String tokenHeader){
        ResponseEntity<UserDto> response = tokenService.callValidateToken(tokenHeader);
        UserDto userDto = response.getBody();

        Cart cart = cartService.removeAllItemsFromCart(userDto.getUserId());
        return ResponseEntity.ok(cart);
    }

}
