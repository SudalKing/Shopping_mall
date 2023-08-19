package com.example.shoppingmall.domain.product.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class Timeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @NotNull
    private LocalDateTime createdAt;

    @Builder
    public Timeline(Long id, Long userId, Long productId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId);
        this.productId = Objects.requireNonNull(productId);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
