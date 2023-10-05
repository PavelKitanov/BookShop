package com.ebookstore.microservices.recommendationservice.services;

import com.ebookstore.microservices.recommendationservice.dto.BookDto;

import java.util.List;
import java.util.Set;

public interface RecommendationService {
    List<BookDto> popularityBasedRecommenderSystem(List<BookDto> books);
    List<BookDto> itemBasedRecommenderSystem(List<BookDto> customerBooks, List<BookDto> allBooks);
}
