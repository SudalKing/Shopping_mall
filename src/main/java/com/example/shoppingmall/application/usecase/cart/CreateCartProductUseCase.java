package com.example.shoppingmall.application.usecase.cart;

import com.example.shoppingmall.domain.cart.dto.req.CartProductRequest;
import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.service.CartProductWriteService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreateCartProductUseCase {
    private final ProductReadService productReadService;
    private final CartReadService cartReadService;
    private final CartProductWriteService cartProductWriteService;

    @Transactional
    public void execute(User user, List<CartProductRequest> cartProductRequests){
        Cart cart = cartReadService.getCartInfo(user.getId());

        for (CartProductRequest cartProductRequest: cartProductRequests) {
            Product product = productReadService.getProductEntity(cartProductRequest.getProductId());

            cartProductWriteService.createCartProduct(cart, product, cartProductRequest.getAmount());
        }
    }
}
