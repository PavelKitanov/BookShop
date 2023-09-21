package com.ebookstore.microservices.bookservice.models;

import com.ebookstore.microservices.bookservice.enumerations.Discount;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long customerId;

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false, referencedColumnName = "cartId")
    private Cart cart;

    @Enumerated
    private Discount discount;

    private double orderTotalPrice;

    public Order(Long customerId,Cart cart, Discount discount){
        this.customerId = customerId;
        this.cart = cart;
        this.discount = discount;
    }

}
