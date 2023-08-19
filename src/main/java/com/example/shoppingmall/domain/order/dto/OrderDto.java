package com.example.shoppingmall.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderDto {
    private Long orderId;
    private Long userId;
    private Long orderStatusId;
    private LocalDateTime createdAt;
    private int totalPrice;

    public OrderDto(Long orderId, Long userId, Long orderStatusId, LocalDateTime createdAt, int totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatusId = orderStatusId;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", orderStatusId=" + orderStatusId +
                ", createdAt=" + createdAt +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
