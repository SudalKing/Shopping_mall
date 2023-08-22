package com.example.shoppingmall.domain.cart.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {
    @Autowired
    private CartReadService cartReadService;

    @DisplayName("1. [Cart 물품 정보 조회]")
    @Test
    void test_1(){
        System.out.println(cartReadService.getCartInfo(5L).toString());
    }
}