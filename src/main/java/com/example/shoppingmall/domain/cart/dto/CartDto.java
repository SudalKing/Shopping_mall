package com.example.shoppingmall.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartDto {
    private Long id;
    private Long userId;
    private int totalPrice;

    public CartDto(Long id, Long userId, int totalPrice) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "CartDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
