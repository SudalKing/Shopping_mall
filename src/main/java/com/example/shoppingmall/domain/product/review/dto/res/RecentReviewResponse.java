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
public class RecentReviewResponse {
    private Long reviewId;
    private String userName;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private int price;
    private String content;
    private String reviewImageUrl;
    private Integer reviewLikeCount;
    private boolean liked;
    private LocalDateTime createdAt;

    public void setLiked() {
        this.liked = true;
    }
}
