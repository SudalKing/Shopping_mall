package com.example.shoppingmall.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCommand {
    private Long userId;
    private String title;
    private String contents;
}
