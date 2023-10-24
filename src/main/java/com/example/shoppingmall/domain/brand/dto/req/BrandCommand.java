package com.example.shoppingmall.domain.brand.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BrandCommand {
    private String name;
    private String pathName;
    private String imageUrl;
}
