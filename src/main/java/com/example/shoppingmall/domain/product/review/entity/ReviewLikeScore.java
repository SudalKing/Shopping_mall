package com.example.shoppingmall.domain.product.review.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class ReviewLikeScore {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reviewId;
    private Double reviewScore;
    private LocalDateTime updatedAt;

    public void setReviewScore(Double reviewScore) {
        this.reviewScore = reviewScore;
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
