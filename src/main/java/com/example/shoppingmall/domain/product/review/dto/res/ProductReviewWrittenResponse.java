package com.example.shoppingmall.domain.product.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductReviewWrittenResponse {
    private Long reviewId;
    private Long orderId;
    private Long productId;
    private String name;
    private String color;
    private String size;
    private String imageUrl;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
}
