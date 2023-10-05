package com.ebookstore.microservices.recommendationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemBasedRecommendationRequest {
    List<BookDto> allBooks;
    List<BookDto> customerBooks;
}
