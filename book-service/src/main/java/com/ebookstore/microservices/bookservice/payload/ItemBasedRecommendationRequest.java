package com.ebookstore.microservices.bookservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ebookstore.microservices.bookservice.dto.BookDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemBasedRecommendationRequest {
    List<BookDto> allBooks;
    List<BookDto> customerBooks;
}