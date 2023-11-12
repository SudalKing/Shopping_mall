package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.product.review.repository.ProductReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductReviewLikeReadService {
    private final ProductReviewLikeRepository productReviewLikeRepository;

    public Integer getReviewLikeCount(Long reviewId) {
        return productReviewLikeRepository.countAllByReviewId(reviewId);
    }
}
