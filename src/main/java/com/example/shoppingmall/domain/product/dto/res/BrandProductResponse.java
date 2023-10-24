package com.example.shoppingmall.domain.product.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BrandProductResponse {

    @Schema(description = "상품 id")
    private Long id;

    @Schema(description = "상품 이름")
    private String name;

    @Schema(description = "상품 가격(0 이상)")
    private int price;

    @Schema(description = "상품 재고(0 이상)")
    private int stock;

    @Schema(description = "상품 설명")
    private String description;

    @Schema(description = "상품 타입 아이디(여러 탭)")
    private Long typeId;

    @Schema(description = "상품 카테고리 아이디(하위 항목)")
    private Long brandCategoryId;

    @Schema(description = "할인 적용 여부")
    private boolean saled;

    @Schema(description = "soft delete")
    private boolean deleted;

    @Schema(description = "상품 좋아요 수")
    private int likeCount;

    @Schema(description = "상품 업로드 이미지 url")
    private List<String> imageUrls = new ArrayList<>();
}
