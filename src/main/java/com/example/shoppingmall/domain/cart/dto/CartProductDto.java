package com.example.shoppingmall.domain.cart.dto;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CartProductDto {
    private Long id;
    private Long cartId;
    private Long productId;
    private int count;
    private LocalDateTime createdAt;
    private boolean enabled;
}
