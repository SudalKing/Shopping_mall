package com.example.shoppingmall.domain.follow.entity;

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
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long followerId;

    @NotNull
    private Long followingId;

    @NotNull
    private LocalDateTime createdAt;

    @Builder
    public Follow(Long id, Long followerId, Long followingId, LocalDateTime createdAt) {
        this.id = id;
        this.followerId = Objects.requireNonNull(followerId);
        this.followingId = Objects.requireNonNull(followingId);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
