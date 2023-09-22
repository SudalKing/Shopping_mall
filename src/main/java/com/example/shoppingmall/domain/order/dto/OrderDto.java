package com.example.shoppingmall.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderDto {
    private Long orderId;
    private Long cartId;
    private Long userId;
    private Long orderStatusId;
    private LocalDateTime createdAt;
    private int totalPrice;

    public OrderDto(Long orderId, Long cartId, Long userId, Long orderStatusId, LocalDateTime createdAt, int totalPrice) {
        this.orderId = orderId;
        this.cartId = cartId;
        this.userId = userId;
        this.orderStatusId = orderStatusId;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
    }
}
