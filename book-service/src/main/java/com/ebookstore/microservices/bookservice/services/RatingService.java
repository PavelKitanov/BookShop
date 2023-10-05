package com.ebookstore.microservices.bookservice.services;

import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.Rating;

import java.util.List;

public interface RatingService {

    List<Rating> getAllRatings();
    Rating save(Rating rating);
    Rating findRatingByBookAndCustomerId(Book book, Long customerId);
    void delete(Long ratingId);
}
