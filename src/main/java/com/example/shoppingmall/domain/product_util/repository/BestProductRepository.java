package com.example.shoppingmall.domain.product_util.repository;

import com.example.shoppingmall.domain.product_util.entity.BestProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestProductRepository extends JpaRepository<BestProduct, Long> {
    BestProduct findTopByOrderByTotalSalesDesc();
    BestProduct findBestByProductId(Long productId);
}
