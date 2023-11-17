package com.example.shoppingmall.domain.product.product.dto.res;

import com.example.shoppingmall.domain.brand.util.BrandInfoMapping;
import com.example.shoppingmall.domain.product.product.dto.ClothesInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private List<ClothesInfo> clothesInfoList;
    private String description;
    private BrandInfoMapping brandInfo;
    private boolean isLiked;
    private Integer price;
    private Integer discountPrice;

    public void setLiked() {
        this.isLiked = true;
    }
}
