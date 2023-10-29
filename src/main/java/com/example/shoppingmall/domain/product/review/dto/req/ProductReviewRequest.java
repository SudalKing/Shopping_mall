package com.example.shoppingmall.domain.product.review.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "상품 댓글 class")
public class ProductReviewRequest {
    private Long orderId;
    private Long productId;
    private String contents;
    private Integer rating;
    private String imageUrl;
}
