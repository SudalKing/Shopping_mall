package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.CreateOrderUseCase;
import com.example.shoppingmall.domain.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;

}
