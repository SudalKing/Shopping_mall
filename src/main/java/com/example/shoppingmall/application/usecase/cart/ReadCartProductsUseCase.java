package com.example.shoppingmall.application.usecase.cart;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.service.CartProductReadService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.product.product.dto.res.ProductInCartReadResponse;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReadCartProductsUseCase {
    private final UserReadService userReadService;
    private final ProductReadService productReadService;
    private final CartReadService cartReadService;
    private final CartProductReadService cartProductReadService;

    @Transactional
    public List<ProductInCartReadResponse> execute(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());

        List<Long> productIds = cartProductReadService.getProductIdsInCart(cart);
        return productReadService.getProductsInCartByIds(productIds);
    }
}
