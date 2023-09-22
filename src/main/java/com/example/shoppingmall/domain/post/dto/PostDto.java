package com.example.shoppingmall.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private Long likeCount;
    private List<String> urls = new ArrayList<>();

    public PostDto(Long id, String title, String contents, LocalDateTime createdAt, Long likeCount, List<String> urls) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.urls = urls;
    }
}
