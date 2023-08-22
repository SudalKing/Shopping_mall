package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.application.usecase.CreateOrderUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderReadService orderReadService;

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @DisplayName("1. [주문 정보 생성 테스트]")
    @Test
    void test_1(){
        createOrderUseCase.execute(5L);
//        createOrderUseCase.execute(4L);
    }


    @DisplayName("2. [주문 정보 읽어오기 테스트]")
    @Test
    void test_2(){
        // 1. 장바구니를 채운다
        // 2. 가격 정보들은 장바구니에서 보여줘야하지 않나?? Order가 아니라!!
        var order = orderReadService.getCurrentOrder(4L, 8L);
        System.out.println(order.toString());
    }

}
