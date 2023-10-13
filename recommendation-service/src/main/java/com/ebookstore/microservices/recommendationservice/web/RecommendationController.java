package com.ebookstore.microservices.recommendationservice.web;

import com.ebookstore.microservices.recommendationservice.dto.BookDto;
import com.ebookstore.microservices.recommendationservice.dto.ItemBasedRecommendationRequest;
import com.ebookstore.microservices.recommendationservice.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/popularityBased")
    public ResponseEntity<List<BookDto>> popularityBasedRecommenderSystem(@RequestBody List<BookDto> books){
        List<BookDto> recommendedBooks = recommendationService.popularityBasedRecommenderSystem(books);
        return ResponseEntity.ok(recommendedBooks);
    }

    @PostMapping("/itemBased")
    public ResponseEntity<List<BookDto>> itemBasedRecommenderSystem(@RequestBody ItemBasedRecommendationRequest request){
        List<BookDto> recommendedBooks = recommendationService.itemBasedRecommenderSystem(request.getCustomerBooks(), request.getAllBooks());
        return ResponseEntity.ok(recommendedBooks);
    }
}
