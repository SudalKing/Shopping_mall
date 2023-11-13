package com.example.shoppingmall.domain.product.review.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReviewStatsResponse {
    private Integer totalCount;
    private Double averageRating;
    private List<Integer> proportion;
    private Integer photoReviewCount;
}
