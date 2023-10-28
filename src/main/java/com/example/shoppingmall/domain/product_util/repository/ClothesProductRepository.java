package com.example.shoppingmall.domain.product_util.repository;

import com.example.shoppingmall.domain.product_util.entity.ClothesProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesProductRepository extends JpaRepository<ClothesProduct, Long> {
    ClothesProduct findByProductId(Long productId);
}
