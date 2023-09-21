package com.ebookstore.microservices.bookservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @JoinColumn( name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    private int quantity;

    private Long customerId;

    public CartItem(Book book, int quantity, Long customerId){
        this.book = book;
        this.quantity = quantity;
        this.customerId = customerId;
    }
}
