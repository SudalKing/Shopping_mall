package com.example.shoppingmall.domain.product.review.repository;

import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    ProductReview findProductReviewById(Long id);
    List<ProductReview> findAllByUserId(Long userId);
    List<ProductReview> findAllByProductId(Long productId);

}
