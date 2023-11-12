package com.example.shoppingmall.domain.product.review.repository;

import com.example.shoppingmall.domain.product.review.entity.ProductReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductReviewLikeRepository extends JpaRepository<ProductReviewLike, Long> {
    Long countAllById(Long id);
    Optional<ProductReviewLike> findByUserIdAndReviewId(Long userId, Long productReviewId);

    Integer countAllByReviewId(Long reviewId);
}
