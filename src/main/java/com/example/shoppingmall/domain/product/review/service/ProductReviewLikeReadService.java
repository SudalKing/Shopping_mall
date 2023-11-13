package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.product.review.entity.ProductReviewLike;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductReviewLikeReadService {
    private final ProductReviewLikeRepository productReviewLikeRepository;

    public Integer getReviewLikeCount(Long reviewId) {
        return productReviewLikeRepository.countAllByReviewId(reviewId);
    }

    public Optional<ProductReviewLike> getByUserIdAndReviewId(Long userId, Long reviewId) {
        return productReviewLikeRepository.findByUserIdAndReviewId(userId, reviewId);
    }
}
