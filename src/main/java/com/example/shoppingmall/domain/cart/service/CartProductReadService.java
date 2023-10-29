package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartProductReadService {
    private final CartProductRepository cartProductRepository;


    public List<CartProduct> getCartProductsEntityByCart(Cart cart){
        return cartProductRepository.findCartProductsByCartIdAndEnabledTrue(cart.getId());
    }

    public List<Long> getProductIdsInCart(Cart cart) {
        return cartProductRepository.findProductIdsByCartId(cart.getId());
    }

}
