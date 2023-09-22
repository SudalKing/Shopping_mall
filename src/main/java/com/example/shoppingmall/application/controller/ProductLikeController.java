package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.product.CreateProductCommentLikeUseCase;
import com.example.shoppingmall.application.usecase.product.CreateProductLikeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductLikeController {
    private final CreateProductLikeUseCase createProductLikeUseCase;
    private final CreateProductCommentLikeUseCase createProductCommentLikeUseCase;


    @Operation(summary = "상품 좋아요",
            description = "상품의 productId와 사용자의 userId를 받아 좋아요 생성", tags = {"USER_ROLE"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @PostMapping("/{productId}/like")
    public void productLike(@PathVariable Long productId, @RequestParam Long userId){
        createProductLikeUseCase.execute(productId, userId);
    }


    @Operation(summary = "상품 댓글 좋아요",
            description = "상품 댓글의 productCommentId와 사용자의 userId를 받아 좋아요 생성", tags = {"USER_ROLE"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @PostMapping("/{productCommentId}/comment/like")
    public void productCommentLike(@PathVariable Long productCommentId, @RequestParam Long userId){
        createProductCommentLikeUseCase.execute(productCommentId, userId);
    }

}
