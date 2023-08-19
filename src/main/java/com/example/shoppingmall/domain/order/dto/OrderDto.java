package com.example.shoppingmall.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderDto {
    private Long id;
    private Long userId;
    private Long cartId;
    private Long orderStatusId;
    private int totalPrice;
    private LocalDateTime createdAt;

    public OrderDto(Long id, Long userId, Long cartId, Long orderStatusId, int totalPrice, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.cartId = cartId;
        this.orderStatusId = orderStatusId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }
}
