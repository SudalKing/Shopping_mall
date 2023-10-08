package com.example.shoppingmall.domain.product_util.repository;

import com.example.shoppingmall.domain.product_util.dto.BrandCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandCategory, Long> {
}
