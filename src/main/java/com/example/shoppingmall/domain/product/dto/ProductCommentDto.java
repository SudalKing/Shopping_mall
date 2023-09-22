package com.example.shoppingmall.domain.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class ProductCommentDto {
    private Long id;
    private Long productId;
    private Long userId;
    private String contents;
    private Long likeCount;
    private LocalDateTime createdAt;

    public ProductCommentDto(Long id, Long productId, Long userId, String contents, Long likeCount, LocalDateTime createdAt) {
        this.id = id;
        this.productId = Objects.requireNonNull(productId);
        this.userId = Objects.requireNonNull(userId);
        this.contents = Objects.requireNonNull(contents);
        this.likeCount = likeCount == null ? 0 : likeCount;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
