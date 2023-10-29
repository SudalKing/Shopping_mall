package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.entity.ProductReviewLike;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewLikeRepository;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductReviewLikeWriteService {
    private final ProductReviewLikeRepository productReviewLikeRepository;
    private final ProductReviewRepository productReviewRepository;

    public void createOrDeleteProductReviewLike(User user, Long productReviewId){
        Optional<ProductReviewLike> findProductReviewLike = productReviewLikeRepository
                .findByUserIdAndReviewId(user.getId(), productReviewId);
        ProductReview productReview = productReviewRepository.findProductReviewById(productReviewId);

        if (findProductReviewLike.isEmpty()) {
            ProductReviewLike productReviewLike = ProductReviewLike.builder()
                    .productId(productReview.getProductId())
                    .reviewId(productReview.getId())
                    .userId(user.getId())
                    .build();
            productReviewLikeRepository.save(productReviewLike);
        } else {
            productReviewLikeRepository.delete(findProductReviewLike.get());
        }
    }

}