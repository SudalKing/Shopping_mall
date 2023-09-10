package com.example.shoppingmall.application.usecase.user;

import com.example.shoppingmall.domain.cart.dto.CartProductDto;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.service.CartProductReadService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReadUserCartProductUseCase {

    private final UserReadService userReadService;
    private final CartProductReadService cartProductReadService;

//    public List<CartProduct> execute(Long userId){
//        var user = userReadService.getUserEntity(userId);
//        var cart = user.getCart();
//        return cart.getCartProducts();
//    }
    public List<CartProductDto> execute(Long userId){
        var user = userReadService.getUserEntity(userId);
        var cart = user.getCart();
        return cartProductReadService.toDtoList(cart.getCartProducts());
    }
}
