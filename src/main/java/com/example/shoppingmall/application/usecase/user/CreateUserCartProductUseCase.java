package com.example.shoppingmall.application.usecase.user;

import com.example.shoppingmall.domain.cart.service.CartProductWriteService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateUserCartProductUseCase {
    private final UserReadService userReadService;
    private final ProductReadService productReadService;
    private final CartWriteService cartWriteService;
    private final CartReadService cartReadService;
    private final CartProductWriteService cartProductWriteService;

    public void execute(Long userId, Long productId, int count){
        var user = userReadService.getUserEntity(userId);
        var product = productReadService.getProductEntity(productId);
        var cart = cartWriteService.createCart(user);
        cartProductWriteService.createCartProduct(cart, product, count);
        cartReadService.getCartInfo(userId);
    }
}
