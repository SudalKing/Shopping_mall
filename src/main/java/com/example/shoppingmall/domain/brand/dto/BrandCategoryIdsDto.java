package com.example.shoppingmall.domain.brand.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BrandCategoryIdsDto {
    private Long categoryId;
    private Long subCategoryId;
}
