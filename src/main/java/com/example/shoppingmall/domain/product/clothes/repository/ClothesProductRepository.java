package com.example.shoppingmall.domain.product.clothes.repository;

import com.example.shoppingmall.domain.product.clothes.entity.ClothesProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesProductRepository extends JpaRepository<ClothesProduct, Long> {
    ClothesProduct findByProductId(Long productId);
}
