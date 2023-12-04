package com.example.shoppingmall.domain.product.sale.repository;

import com.example.shoppingmall.domain.product.sale.entity.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {
    Optional<ProductSale> findByProductId(Long productId);
}
