package com.example.shoppingmall.domain.product.dto;

import com.example.shoppingmall.domain.product.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCommand {
    private String name;
    private String modelName;
    private int price;
    private int stock;
    private String description;
    private Category category;
}
