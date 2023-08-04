package com.example.shoppingmall.domain.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCommentCommand {
    private Long productId;
    private String contents;
}
