package com.example.shoppingmall.domain.brand.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BrandDetailResponse {
    private Long id;
    private String name;
    private List<Long> categoryIds;
    private String logoUrl;
    private String imageUrl;
    private boolean isLiked;

    public void setLiked() {
        this.isLiked = true;
    }
}
