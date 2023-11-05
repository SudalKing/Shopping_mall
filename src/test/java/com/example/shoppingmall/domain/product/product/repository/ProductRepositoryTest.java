package com.example.shoppingmall.domain.product.product.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findCategoryAndSubCategoryIdsByBrandId() {
        var ids = productRepository.findCategoryIdsByBrandId(1L).get(0);
        System.out.println("subC: " + ids.getCategoryId());
        System.out.println("C: " + ids.getSubCategoryId());
    }
}