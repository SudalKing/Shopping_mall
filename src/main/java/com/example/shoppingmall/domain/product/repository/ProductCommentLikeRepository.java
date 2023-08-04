package com.example.shoppingmall.domain.product.repository;

import com.example.shoppingmall.domain.product.entity.ProductCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCommentLikeRepository extends JpaRepository<ProductCommentLike, Long> {
    Long countAllByCommentId(Long commentId);
}
