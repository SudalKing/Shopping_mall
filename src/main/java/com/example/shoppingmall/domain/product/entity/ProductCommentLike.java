package com.example.shoppingmall.domain.product.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class ProductCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long commentId;
    private LocalDateTime createdAt;

    @Builder
    public ProductCommentLike(Long id, Long userId, Long commentId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId);
        this.commentId = Objects.requireNonNull(commentId);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
