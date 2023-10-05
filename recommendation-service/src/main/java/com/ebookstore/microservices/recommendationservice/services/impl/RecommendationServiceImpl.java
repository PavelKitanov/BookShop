package com.ebookstore.microservices.recommendationservice.services.impl;

import com.ebookstore.microservices.recommendationservice.dto.BookDto;
import com.ebookstore.microservices.recommendationservice.services.RecommendationService;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Override
    public List<BookDto> popularityBasedRecommenderSystem(List<BookDto> books) {
        List<BookDto> sortedList = books.stream()
                .sorted(Comparator.comparingDouble(bookDto -> -calculateAverageRating(bookDto)))
                .collect(Collectors.toList());

        if(sortedList.size() >= 5)
            return sortedList.subList(0,5);
        else
            return sortedList;
    }

    private double calculateAverageRating(BookDto bookDto) {
        List<Integer> ratings = bookDto.getRatings();
        if (ratings.isEmpty()) {
            return 0.0;
        }
        double sum = ratings.stream().mapToInt(Integer::intValue).sum();
        return sum / ratings.size();
    }

    @Override
    public List<BookDto> itemBasedRecommenderSystem(List<BookDto> customerBooks, List<BookDto> allBooks) {
        Set<BookDto> recommended = new HashSet<>();
        for(BookDto book : customerBooks){
            List<BookDto> recommendedByTargetBook = itemBasedRecommenderSystemByTargetBook(book.getBookId(), 5, allBooks);
            recommended.addAll(recommendedByTargetBook);
        }

        for(BookDto book : customerBooks){
            if(recommended.contains(book))
                recommended.remove(book);
        }

        return recommended.stream().sorted(Comparator.comparing(BookDto::getSimilarityScore).reversed()).collect(Collectors.toList()).subList(0,5);
    }

    private List<BookDto> itemBasedRecommenderSystemByTargetBook(Long targetBookId, int numRecommendations, List<BookDto> books) {
        Map<Long, RealVector> bookVectors = books.stream()
                                            .collect(Collectors.toMap(
                                                    BookDto::getBookId,
                                                    book -> createVector(book.getAuthor(), book.getGenre())
                                            ));

        if (!bookVectors.containsKey(targetBookId)) {
            throw new IllegalArgumentException("Target book not found.");
        }

        // Calculate cosine similarity between the target book and all other books
        RealVector targetVector = bookVectors.get(targetBookId);
        CosineDistance cosineDistance = new CosineDistance();
        Map<Long, Double> similarities = new HashMap<>();

        for (Map.Entry<Long, RealVector> entry : bookVectors.entrySet()) {
            if (!entry.getKey().equals(targetBookId)) {
                double similarity = 1 - cosineDistance.apply(targetVector.toString(), entry.getValue().toString());
                similarities.put(entry.getKey(), similarity);
            }
        }

        // Sort books by similarity in descending order
        List<BookDto> recommendedBooks = similarities.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(numRecommendations)
                .map(entry -> {
                    BookDto bookDto = books.stream().filter(b -> b.getBookId().equals(entry.getKey())).findFirst().orElse(null);
                    if (bookDto != null) {
                        bookDto.setSimilarityScore(entry.getValue());
                    }
                    return bookDto;
                })
                .collect(Collectors.toList());

        return recommendedBooks;
    }



    private RealVector createVector(String author, String genre) {
        // Create a vector representation for a book based on author and genre
        double[] vectorData = new double[2];
        vectorData[0] = author.hashCode();
        vectorData[1] = genre.hashCode();

        return new ArrayRealVector(vectorData);
    }
}
