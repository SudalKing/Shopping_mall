package com.example.shoppingmall.domain.product.review.repository;

import com.example.shoppingmall.domain.product.review.entity.ReviewLikeScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeScoreRepository extends JpaRepository<ReviewLikeScore, Long> {
    Optional<ReviewLikeScore> findReviewLikeScoreByReviewId(Long id);
    void deleteByReviewId(Long reviewId);
}
