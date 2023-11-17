package com.example.shoppingmall.domain.product.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ClothesInfo {
    private Long id;
    private String color;
    private String size;
}
