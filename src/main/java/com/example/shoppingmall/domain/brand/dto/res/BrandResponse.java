package com.example.shoppingmall.domain.brand.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BrandResponse {
    private Long id;
    private String name;
    private String pathName;
    private String imageUrl;
}
