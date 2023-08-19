package com.example.shoppingmall.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private Long likeCount;

    public PostDto(Long id, String title, String contents, LocalDateTime createdAt, Long likeCount) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
    }
}
