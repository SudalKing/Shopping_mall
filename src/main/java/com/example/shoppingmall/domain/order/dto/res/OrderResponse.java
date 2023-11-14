package com.example.shoppingmall.domain.order.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderResponse {
    private Long orderId;
    private Long productId;
    private String productName;
    private String color;
    private String size;
    private int price;
    private int discountPrice;
    private int amount;
    private String status;
    private String imageUrl;
    private LocalDateTime createdAt;
}
