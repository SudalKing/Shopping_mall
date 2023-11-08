package com.example.shoppingmall.domain.brand.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void findLikeBrandsOrderByLike() {
        var findBrandList = brandRepository.findLikeBrandsOrderByLike(1L);
        findBrandList.ifPresent(System.out::println);
    }
}