package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.CreateProductCommentLikeUseCase;
import com.example.shoppingmall.application.usecase.CreateProductLikeUseCase;
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

    @PostMapping("/{productId}/like")
    public void productLike(@PathVariable Long productId, @RequestParam Long userId){
        createProductLikeUseCase.execute(productId, userId);
    }

    @PostMapping("/{productCommentId}/comment/like")
    public void productCommentLike(@PathVariable Long productCommentId, @RequestParam Long userId){
        createProductCommentLikeUseCase.execute(productCommentId, userId);
    }

}
