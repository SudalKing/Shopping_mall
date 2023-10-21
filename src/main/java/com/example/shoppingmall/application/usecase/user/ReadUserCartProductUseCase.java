package com.example.shoppingmall.application.usecase.user;

import com.example.shoppingmall.domain.cart.dto.CartProductDto;
import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.service.CartProductReadService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReadUserCartProductUseCase {
    private final CartReadService cartReadService;
    private final CartProductReadService cartProductReadService;

//    public List<CartProduct> execute(Long userId){
//        var user = userReadService.getUserEntity(userId);
//        var cart = user.getCart();
//        return cart.getCartProducts();
//    }
    public List<CartProductDto> execute(User user){
        Cart cart = cartReadService.getCartInfo(user.getId());
        return cartProductReadService.getCartProductsByCart(cart);
    }
}
