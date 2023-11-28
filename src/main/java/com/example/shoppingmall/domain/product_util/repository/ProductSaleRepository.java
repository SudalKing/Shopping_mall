package com.example.shoppingmall.domain.product_util.repository;

import com.example.shoppingmall.domain.product_util.entity.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {
    Optional<ProductSale> findByProductId(Long productId);
}
