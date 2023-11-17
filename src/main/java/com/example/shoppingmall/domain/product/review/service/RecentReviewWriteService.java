package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.product.review.entity.RecentReview;
import com.example.shoppingmall.domain.product.review.repository.RecentReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class RecentReviewWriteService {
    private final RecentReviewRepository recentReviewRepository;

    public void createRecentReview(Long userId, Long reviewId) {
        RecentReview recentReview = RecentReview.builder()
                .reviewId(reviewId)
                .userId(userId)
                .build();
        recentReviewRepository.save(recentReview);
    }

}
