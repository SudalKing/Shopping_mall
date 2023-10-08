package com.example.shoppingmall.application.usecase.product;

import com.example.shoppingmall.domain.product_util.service.BestReadService;
import com.example.shoppingmall.domain.product_util.service.BestWriteService;
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
    private final BestReadService bestReadService;
    private final BestWriteService bestWriteService;

    public void execute(Long productId, Long userId){
        var product = productReadService.getProduct(productId);
        var user = userReadService.getUser(userId);

        //  스케쥴러 분리
        bestWriteService.updateTotalLike(productId);

        productLikeWriteService.createProductLike(product, user);
    }
}