package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.product.review.dto.res.ProductReviewWriteableResponse;
import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewLikeRepository;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductReviewReadService {
    private final ProductReviewRepository productReviewRepository;

    public List<ProductReview> getAllReviewsByUserId(Long userId) {
        return productReviewRepository.findAllByUserId(userId);
    }

    public ProductReview getReviewByReviewId(Long reviewId) {
        return productReviewRepository.findProductReviewById(reviewId);
    }
}
