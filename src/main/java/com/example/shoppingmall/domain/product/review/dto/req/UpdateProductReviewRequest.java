package com.example.shoppingmall.domain.product.review.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UpdateProductReviewRequest {
    private String content;
    private Integer rating;
}
