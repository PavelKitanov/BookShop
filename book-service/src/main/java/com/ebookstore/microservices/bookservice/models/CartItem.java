package com.ebookstore.microservices.bookservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jfr.BooleanFlag;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = true)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    private int quantity;

    private Long customerId;

    @BooleanFlag
    private boolean isOrdered;

    public CartItem(Book book, int quantity, Long customerId){
        this.book = book;
        this.quantity = quantity;
        this.customerId = customerId;
        this.isOrdered = false;
    }
}
