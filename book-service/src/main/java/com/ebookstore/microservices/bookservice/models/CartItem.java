package com.ebookstore.microservices.bookservice.models;

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

    @OneToOne
    private Book book;

    private int quantity;

    public CartItem(Book book, int quantity){
        this.book = book;
        this.quantity = quantity;
    }
}
