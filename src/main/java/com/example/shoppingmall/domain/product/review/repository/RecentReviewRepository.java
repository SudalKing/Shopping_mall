package com.example.shoppingmall.domain.product.review.repository;

import com.example.shoppingmall.domain.product.review.entity.RecentReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentReviewRepository extends JpaRepository<RecentReview, Long> {
}
