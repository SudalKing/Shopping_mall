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
public class ReviewListResponse {
    private Long reviewId;
    private String userName;
    private String color;
    private String size;
    private Integer rating;
    private String reviewImageUrl;
    private Integer reviewLikeCount;
    private LocalDateTime createdAt;
}