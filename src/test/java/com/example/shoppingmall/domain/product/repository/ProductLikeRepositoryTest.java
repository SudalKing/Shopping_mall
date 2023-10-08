package com.example.shoppingmall.domain.product.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductLikeRepositoryTest {

    @Autowired
    private ProductLikeRepository productLikeRepository;

    @Test
    void countAllByProductId() {
        Long productId = 100L;
        var count = productLikeRepository.countAllByProductId(productId);
        System.out.println(productId + "의 like 개수: " + count);
    }

    @Test
    void a(){
        var productIdMostLike = productLikeRepository.findProductIdMostLike();
        System.out.println("가장 좋아요가 많은 품목의 id:" + productIdMostLike);

    }

    @Test
    void b(){
        var productLikeCount = productLikeRepository.findLikeCountMostLike();
        System.out.println("가장 좋아요가 많은 품목의 좋아요 개수:" + productLikeCount);
    }

    @Test
    void c(){
        var productLikeCount = productLikeRepository.findLikeCountByProductId(313L);
        System.out.println("현재 상품 좋아요 개수: " + productLikeCount);
    }
}