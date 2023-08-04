package com.example.shoppingmall.domain.product.repository;

import com.example.shoppingmall.domain.product.entity.ProductComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {
    ProductComment findProductCommentById(Long id);
    List<ProductComment> findAllByProductId(Long productId);
}
