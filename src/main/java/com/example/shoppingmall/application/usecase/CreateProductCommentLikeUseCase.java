package com.example.shoppingmall.application.usecase;

import com.example.shoppingmall.domain.product.service.ProductCommentLikeWriteService;
import com.example.shoppingmall.domain.product.service.ProductCommentReadService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateProductCommentLikeUseCase {
    private final ProductCommentLikeWriteService productCommentLikeWriteService;
    private final UserReadService userReadService;
    private final ProductCommentReadService productCommentReadService;

    public void execute(Long productCommentId, Long userId){
        var productComment = productCommentReadService.getComment(productCommentId);
        var user = userReadService.getUser(userId);

        productCommentLikeWriteService.createProductCommentLike(productComment, user);
    }
}