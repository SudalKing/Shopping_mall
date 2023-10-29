package com.example.shoppingmall.domain.product.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductReviewDetailResponse {
    private Long reviewId;
    private Long productId;
    private String content;
    private Integer rating;
    private String name;
    private String color;
    private String size;
    private String productImageUrl;
    private String reviewImageUrl;
}
