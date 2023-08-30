package com.ebookstore.microservices.bookservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @ManyToMany
    private List<CartItem> cartItems;

    public Cart(Long customerId) {
        this.customerId = customerId;
        this.cartItems = new ArrayList<>();
    }

    public void addItemToCart(CartItem cartItem){
        this.cartItems.add(cartItem);
    }

    public void removeItemFromCart(CartItem cartItem){
        this.cartItems.remove(cartItem);
    }

}
