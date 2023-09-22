package com.example.shoppingmall.domain.awsS3.repository;

import com.example.shoppingmall.domain.awsS3.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProductId(Long productId);
    void deleteAllByProductId(Long productId);
}
