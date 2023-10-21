package com.example.shoppingmall.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderDto {
    private Long orderId;
    private Long userId;
    private Long orderStatusId;
    private LocalDateTime createdAt;
    private Long versionCount;
    private int totalPrice;

}
