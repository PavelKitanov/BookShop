package com.ebookstore.microservices.bookservice.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY)
    private Long ratingId;

    private int rating;

    private Long customerId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnore
    private Book book;

    public Rating(int rating, Long customerId, Book book){
        this.rating = rating;
        this.customerId = customerId;
        this.book = book;
    }
}
