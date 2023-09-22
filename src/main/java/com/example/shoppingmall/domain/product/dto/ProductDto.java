package com.example.shoppingmall.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Schema(description = "상품 Response class")
public class ProductDto {

    @Schema(description = "상품 id")
    private Long id;

    @Schema(description = "상품 이름")
    private String name;

    @Schema(description = "상품 모델명")
    private String modelName;

    @Schema(description = "상품 가격(0 이상)")
    private int price;

    @Schema(description = "상품 재고(0 이상)")
    private int stock;

    @Schema(description = "상품 설명")
    private String description;

    @Schema(description = "상품 카테고리 아이디")
    private Long categoryId;

    @Schema(description = "soft delete")
    private boolean deleted;

    @Schema(description = "상품 좋아요 수")
    private Long likeCount;

    @Schema(description = "상품 업로드 이미지 url")
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
