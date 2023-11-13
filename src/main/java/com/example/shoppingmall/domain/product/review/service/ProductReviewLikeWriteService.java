package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.entity.ProductReviewLike;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewLikeRepository;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewRepository;
import com.example.shoppingmall.domain.product.review.repository.ReviewLikeScoreRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductReviewLikeWriteService {
    private final ProductReviewLikeRepository productReviewLikeRepository;
    private final ProductReviewRepository productReviewRepository;
    private final ProductReviewWriteService productReviewWriteService;

    @Transactional
    public void createOrDeleteProductReviewLike(User user, Long productReviewId){
        Optional<ProductReviewLike> findProductReviewLike = productReviewLikeRepository
                .findByUserIdAndReviewId(user.getId(), productReviewId);
        ProductReview productReview = productReviewRepository.findProductReviewById(productReviewId);

        if (findProductReviewLike.isEmpty()) {
            ProductReviewLike productReviewLike = ProductReviewLike.builder()
                    .productId(productReview.getProductId())
                    .reviewId(productReview.getId())
                    .userId(user.getId())
                    .createdAt(LocalDateTime.now())
                    .build();
            productReviewLikeRepository.save(productReviewLike);

            productReviewWriteService.updateReviewLikeScore(productReviewId);
        } else {
            productReviewLikeRepository.delete(findProductReviewLike.get());

            productReviewWriteService.updateReviewLikeScore(productReviewId);
        }
    }

}
