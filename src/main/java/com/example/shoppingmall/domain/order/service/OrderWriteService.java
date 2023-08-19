package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class OrderWriteService {
    private final OrdersRepository ordersRepository;

    public Orders createOrder(Long userId, Long cartId){
        var orders = Orders.builder()
                .userId(userId)
                .cartId(cartId)
                .orderStatusId(1L) // 기본 1(주문 완료)로 저장 -> 이후 배송 중, 배송 완료 등으로 바꿈
                .createdAt(LocalDateTime.now())
                .orderProducts(new ArrayList<>())
                .build();
        return ordersRepository.save(orders);
    }
}
