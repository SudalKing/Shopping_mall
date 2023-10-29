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
public class ProductDto {

    @Schema(description = "상품 id")
    private Long id;

    @Schema(description = "상품 이름")
    private String name;

    @Schema(description = "상품 가격(0 이상)")
    private Integer price;

    private Double score;

    @Schema(description = "상품 설명")
    private String description;

    @Schema(description = "상품 업로드 이미지 url")
    private String imageUrl;

    @Schema(description = "좋아요 여부")
    private boolean isLiked;

    @Schema(description = "브랜드 정보")
    private BrandInfoMapping brandInfo;

    public void setLiked(){
        this.isLiked = true;
    }
}
