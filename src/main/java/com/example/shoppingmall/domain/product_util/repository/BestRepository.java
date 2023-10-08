package com.example.shoppingmall.domain.product_util.repository;

import com.example.shoppingmall.domain.product_util.dto.Best;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestRepository extends JpaRepository<Best, Long> {
    Best findTopByOrderByTotalSalesDesc();
    Best findBestByProductId(Long productId);
}
