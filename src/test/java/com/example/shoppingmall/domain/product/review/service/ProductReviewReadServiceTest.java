package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.product.review.repository.ProductReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductReviewReadServiceTest {

    @Autowired
    private ProductReviewReadService productReviewReadService;

    @Test
    void getAllReviewsByUserIdOrderByCreatedAt() {
    }

    @Test
    void getReviewByReviewId() {
    }

    @Test
    void getReviewStatsByProductId() {
        var reviewStatsRes = productReviewReadService.getReviewStatsByProductId(31L);
        var proportion = reviewStatsRes.getProportion();
        System.out.println("totalCount: " + reviewStatsRes.getTotalCount());
        System.out.println("avgRating: " + reviewStatsRes.getAverageRating());
        System.out.println("photoCount: " + reviewStatsRes.getPhotoReviewCount());

        for (Integer count: proportion) {
            System.out.println("proportion: " + count);
        }

    }
}