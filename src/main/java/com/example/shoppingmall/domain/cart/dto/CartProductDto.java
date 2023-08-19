package com.example.shoppingmall.domain.cart.dto;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CartProductDto {
    private Long id;
    private Cart cart;
    private Product product;
    private int count;
    private LocalDateTime createdAt;

    public CartProductDto(Long id, Cart cart, Product product, int count, LocalDateTime createdAt) {
        this.id = id;
        this.cart = cart;
        this.product = product;
        this.count = count;
        this.createdAt = createdAt;
    }
}
