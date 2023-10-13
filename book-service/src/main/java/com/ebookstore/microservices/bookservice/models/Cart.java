package com.ebookstore.microservices.bookservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long cartId;

    private Long customerId;

    @JsonIgnore
    @OneToOne(mappedBy = "cart",fetch = FetchType.LAZY,
    cascade =  CascadeType.ALL)
    private Order order;

    @OneToMany
    @JoinColumn(name = "cart_id")
    private List<CartItem> cartItems;

    private double cartTotalPrice;

    private boolean isActive;

    public Cart(Long customerId) {
        this.customerId = customerId;
        this.cartItems = new ArrayList<>();
        this.cartTotalPrice = 0;
        this.isActive = true;
    }

    public void addItemToCart(CartItem cartItem){
        this.cartItems.add(cartItem);
        this.updateTotalPrice();
    }

    public void removeItemFromCart(CartItem cartItem){
        this.cartItems.remove(cartItem);
        this.updateTotalPrice();
    }

    public void updateTotalPrice(){
        this.cartTotalPrice = this.cartItems.stream().mapToDouble(i -> i.getBook().getPrice() * i.getQuantity()).sum();
    }

}
