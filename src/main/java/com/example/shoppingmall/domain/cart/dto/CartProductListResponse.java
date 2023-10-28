package com.example.shoppingmall.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CartProductListResponse {
    private Long id;
    private Long cartId;
    private Long productId;
    private int count;
    private LocalDateTime createdAt;
    private boolean enabled;
}
