package com.example.shoppingmall.domain.brand.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BrandCategoryRequest {
    private Long brandId;
    private Long categoryId;
}
