package com.ebookstore.microservices.recommendationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long bookId;
    private String title;
    private String author;
    private String genre;
    private List<Integer> ratings;
    private double similarityScore;

    public void setSimilarityScore(double similarityScore) {
        if(this.similarityScore < similarityScore){
            this.similarityScore = similarityScore;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(author, bookDto.author) &&
                Objects.equals(title, bookDto.title) &&
                Objects.equals(genre, bookDto.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title, genre);
    }

}

