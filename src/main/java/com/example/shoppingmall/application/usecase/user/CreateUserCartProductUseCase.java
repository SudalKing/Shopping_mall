package com.example.shoppingmall.application.usecase.user;

import com.example.shoppingmall.domain.cart.service.CartProductWriteService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateUserCartProductUseCase {
    private final ProductReadService productReadService;
    private final CartWriteService cartWriteService;
    private final CartReadService cartReadService;
    private final CartProductWriteService cartProductWriteService;

    public void execute(User user, Long productId){
        var product = productReadService.getProductEntity(productId);
        var cart = cartWriteService.createCart(user);
        cartProductWriteService.createCartProduct(cart, product);
        cartReadService.getCartInfo(user.getId());
    }
}
