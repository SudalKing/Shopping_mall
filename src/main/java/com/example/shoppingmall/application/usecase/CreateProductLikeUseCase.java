package com.example.shoppingmall.application.usecase;

import com.example.shoppingmall.domain.product.service.ProductLikeWriteService;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateProductLikeUseCase {
    private final ProductLikeWriteService productLikeWriteService;
    private final UserReadService userReadService;
    private final ProductReadService productReadService;

    public void execute(Long productId, Long userId){
        var product = productReadService.getProduct(productId);
        var user = userReadService.getUser(userId);

        productLikeWriteService.createProductLike(product, user);
    }
}