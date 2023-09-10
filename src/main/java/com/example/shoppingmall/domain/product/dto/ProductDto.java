package com.example.shoppingmall.domain.product.dto;

import com.example.shoppingmall.domain.product.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ProductDto {
    private Long id;
    private String name;
    private String modelName;
    private int price;
    private int stock;
    private String description;
    private Long categoryId;
    private boolean deleted;
    private Long likeCount;
    private List<String> urls = new ArrayList<>();

    public ProductDto(Long id, String name, String modelName, int price, int stock,
                      String description, Long categoryId, boolean deleted, Long likeCount, List<String> urls) {
        this.id = id;
        this.name = name;
        this.modelName = modelName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.categoryId = categoryId;
        this.deleted = deleted;
        this.likeCount = likeCount;
        this.urls = urls;
    }
}
