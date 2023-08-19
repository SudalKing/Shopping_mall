package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class CartProductWriteServiceTest {
    @Autowired
    private CartProductWriteService cartProductWriteService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private ProductReadService productReadService;

    @Autowired
    private CartProductRepository cartProductRepository;

    @DisplayName("1. [CartProduct 저장 테스트]")
    @Test
    void test_1() {
        var cp = CartProduct.builder()
                .cart(userReadService.getUserEntity(1L).getCart())
                .product(productReadService.getProductEntity(1L))
                .count(1)
                .createdAt(LocalDateTime.now())
                .build();
        cartProductRepository.save(cp);
    }
}