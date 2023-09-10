package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.product.CreateProductCommentLikeUseCase;
import com.example.shoppingmall.application.usecase.product.CreateProductLikeUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"상품 & 상품댓글 좋아요 기능"})
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductLikeController {
    private final CreateProductLikeUseCase createProductLikeUseCase;
    private final CreateProductCommentLikeUseCase createProductCommentLikeUseCase;

    @ApiOperation(value = "상품 좋아요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "현재 상품의 id", dataType = "Long"),
            @ApiImplicitParam(name = "userId", value = "현재 사용자의 id", dataType = "Long"),
    })
    @Operation(description = "상품과 사용자의 id를 받아 좋아요 생성")
    @PostMapping("/{productId}/like")
    public void productLike(@PathVariable Long productId, @RequestParam Long userId){
        createProductLikeUseCase.execute(productId, userId);
    }

    @ApiOperation(value = "상품 댓글 좋아요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productCommentId", value = "현재 상품 댓글의 id", dataType = "Long"),
            @ApiImplicitParam(name = "userId", value = "현재 사용자의 id", dataType = "Long"),
    })
    @Operation(description = "상품 댓글과 사용자의 id를 받아 좋아요 생성")
    @PostMapping("/{productCommentId}/comment/like")
    public void productCommentLike(@PathVariable Long productCommentId, @RequestParam Long userId){
        createProductCommentLikeUseCase.execute(productCommentId, userId);
    }

}
