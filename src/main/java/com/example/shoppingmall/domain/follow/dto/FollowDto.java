package com.example.shoppingmall.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class FollowDto {
    private Long id;
    private Long followerId;
    private Long followingId;
    private LocalDateTime createdAt;

    public FollowDto(Long id, Long followerId, Long followingId, LocalDateTime createdAt) {
        this.id = id;
        this.followerId = Objects.requireNonNull(followerId);
        this.followingId = Objects.requireNonNull(followingId);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
