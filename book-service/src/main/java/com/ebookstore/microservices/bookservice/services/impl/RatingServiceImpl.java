package com.ebookstore.microservices.bookservice.services.impl;

import com.ebookstore.microservices.bookservice.models.Book;
import com.ebookstore.microservices.bookservice.models.Rating;
import com.ebookstore.microservices.bookservice.repositories.RatingRepository;
import com.ebookstore.microservices.bookservice.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }


    @Override
    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public Rating findRatingByBookAndCustomerId(Book book, Long customerId) {
        return ratingRepository.findRatingByBookAndCustomerId(book, customerId).orElse(null);
    }
    @Override
    public void deleteById(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }
}
