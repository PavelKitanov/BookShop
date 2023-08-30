package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.CartItem;
import com.ebookstore.microservices.bookservice.services.CartItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public List<CartItem> getAll(){
        return cartItemService.findAll();
    }

    @GetMapping("/{id}")
    public CartItem getCartItemById(@PathVariable Long id){
        return cartItemService.findById(id);
    }

    @PostMapping
    public CartItem createCartItem(@RequestParam(required = false) Long id,
                                   @RequestParam Long bookId,
                                   @RequestParam int quantity){
        if(id == null)
            return cartItemService.save(bookId,quantity);
        else
            return cartItemService.update(id, quantity);
    }

    @DeleteMapping("/{id}")
    public void removeCartItem(@PathVariable Long id){
        cartItemService.deleteById(id);
    }

}
