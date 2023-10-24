package com.example.shoppingmall.domain.product.dto.req;

import com.example.shoppingmall.domain.brand.dto.req.BrandCategoryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "상품 등록 Request class")
public class ProductCommand {
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

    @Schema(description = "상품 타입 아이디")
    private Long typeId;

    @Schema(description = "상품 카테고리")
    private BrandCategoryRequest brandCategoryRequest;

    @Schema(description = "할인 적용 여부")
    private boolean saled;
}
