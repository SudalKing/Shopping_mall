package com.example.shoppingmall.domain.product.product_duplicated.repository;

import com.example.shoppingmall.domain.product.product_duplicated.entity.ProductDuplicate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDuplicateRepository extends JpaRepository<ProductDuplicate, Long> {
    List<ProductDuplicate> findAllByName(String name);
    List<ProductDuplicate> findProductDuplicatesByProductIdInOrderByCreatedAtDesc(List<Long> productIds);
    ProductDuplicate findByProductId(Long productId);
}
