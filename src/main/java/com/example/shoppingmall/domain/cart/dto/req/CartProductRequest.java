package com.example.shoppingmall.domain.cart.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CartProductRequest {
    private Long productId;
    private int amount;
}
