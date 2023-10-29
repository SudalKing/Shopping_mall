package com.example.shoppingmall.domain.product.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductReviewWriteableResponse {
    private Long orderId;
    private Long productId;
    private String name;
    private String color;
    private String size;
    private String imageUrl;
}
