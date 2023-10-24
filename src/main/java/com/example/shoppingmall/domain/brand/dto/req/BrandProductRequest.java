package com.example.shoppingmall.domain.brand.dto.req;

import com.example.shoppingmall.util.CursorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BrandProductRequest {
    private CursorRequest cursorRequest;
    private Long brandId;
    private Long brandCategoryId;
    private Long sortId;
}
