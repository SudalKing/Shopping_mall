package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product_util.service.BestWriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BestWriteServiceTest {

    @Autowired
    private BestWriteService bestWriteService;

    @DisplayName("1. [Best 저장]")
    @Test
    void test_1(){
        var score = bestWriteService.calcScore(300L);
        System.out.println(score);
    }

}