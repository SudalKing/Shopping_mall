package com.example.shoppingmall.domain.product.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductOrderResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String color;
    private String size;
    private int price;
    private int amount;
    private int discountPrice;
}
