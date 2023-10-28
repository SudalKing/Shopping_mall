package com.example.shoppingmall.domain.product.repository;

import com.example.shoppingmall.domain.product.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    ProductReview findProductReviewById(Long id);
    List<ProductReview> findAllByProductId(Long productId);
}
