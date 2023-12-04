package com.example.shoppingmall.domain.product.best.repository;

import com.example.shoppingmall.domain.product.best.entity.BestProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestProductRepository extends JpaRepository<BestProduct, Long> {
    BestProduct findTopByOrderByTotalSalesDesc();
    BestProduct findBestByProductId(Long productId);
}
