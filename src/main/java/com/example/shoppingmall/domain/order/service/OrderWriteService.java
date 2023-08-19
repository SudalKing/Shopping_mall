package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.dto.OrderDto;
import com.example.shoppingmall.domain.order.entity.Order;
import com.example.shoppingmall.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OrderWriteService {
    private final OrderRepository orderRepository;

    public Order createOrder(OrderDto orderDto){
        var order = Order.builder()
                .userId(orderDto.getUserId())
                .cartId(orderDto.getCartId())
                .orderStatusId(orderDto.getOrderStatusId())
                .totalPrice(orderDto.getTotalPrice())
                .createdAt(LocalDateTime.now())
                .build();
        return orderRepository.save(order);
    }
}
