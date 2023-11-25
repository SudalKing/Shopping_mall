package com.example.shoppingmall.domain.order.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderProductRepositoryTest {

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Test
    void countAllByOrdersCount() {
        System.out.println(orderProductRepository.countAllByOrdersCount(1L));
        System.out.println(orderProductRepository.countAllByOrdersCountByStatus(1L, 2L));
    }
}