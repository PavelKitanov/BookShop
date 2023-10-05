package com.ebookstore.microservices.bookservice.proxy;

import com.ebookstore.microservices.bookservice.dto.BookDto;
import com.ebookstore.microservices.bookservice.payload.ItemBasedRecommendationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "recommendation-service")
public interface RecommendationProxy {

    @PostMapping("/recommendations/popularityBased")
    ResponseEntity<List<BookDto>> popularityBasedRecommenderSystem(@RequestBody List<BookDto> books);

    @PostMapping("/recommendations/itemBased")
    ResponseEntity<List<BookDto>> itemBasedRecommenderSystem(@RequestBody ItemBasedRecommendationRequest request);
}
