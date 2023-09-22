package com.example.shoppingmall.domain.product.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class ProductComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private Long userId;

    @NotBlank
    private String contents;

    @NotNull
    private LocalDateTime createdAt;

    @Builder
    public ProductComment(Long id, Long productId, Long userId, String contents, LocalDateTime createdAt) {
        this.id = id;
        this.productId = Objects.requireNonNull(productId);
        this.userId = Objects.requireNonNull(userId);
        this.contents = Objects.requireNonNull(contents);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
