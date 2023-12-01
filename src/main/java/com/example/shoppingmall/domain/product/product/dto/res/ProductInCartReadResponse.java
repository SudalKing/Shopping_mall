package com.example.shoppingmall.domain.product.product.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductInCartReadResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String color;
    private String size;
    private int price;
    private int amount;
    private int discountPrice;
    private LocalDateTime createdAt;
}
