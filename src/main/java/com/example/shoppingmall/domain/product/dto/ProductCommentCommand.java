package com.example.shoppingmall.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "상품 댓글 class")
public class ProductCommentCommand {

    @Schema(description = "댓글을 등록하려는 상품의 productId", example = "상품 id")
    private Long productId;

    @Schema(description = "댓글 내용", example = "댓글 내용")
    private String contents;
}
