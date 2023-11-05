package com.example.shoppingmall.domain.product.product.dto;

import com.example.shoppingmall.domain.brand.util.BrandInfoMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@Schema(description = "상품 Response class")
public class ProductResponse {

    private Long id;
    private String name;
    private Integer price;
    private Double score;
    private String description;
    private String imageUrl;
    private boolean isLiked;
    private BrandInfoMapping brandInfo;

    public void setLiked(){
        this.isLiked = true;
    }
}
