package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartProductWriteService {
    private final CartProductRepository cartProductRepository;

    // 1. User 별로 Cart 부여
    // 2. 주문이 완료되면 CartProducts 의 enabled = false 변경

    @Transactional
    public void createCartProduct(Cart cart, Product product){
        var findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());

        if(findCartProduct == null){
            var cartProduct = CartProduct.builder()
                    .cartId(cart.getId())
                    .productId(product.getId())
                    .count(1)
                    .createdAt(LocalDateTime.now())
                    .enabled(true)
                    .build();
            cartProductRepository.save(cartProduct);
        } else {findCartProduct.addCount(1);}
    }

    @Transactional
    public void minusCartProductCount(Cart cart, Product product) throws Exception {
        var findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());

        if (findCartProduct.getCount() > 1) {
            findCartProduct.minusCount(1);
        } else {
            throw new Exception("Can not minus!!");
        }
    }

    @Transactional
    public void deleteCartProduct(Cart cart, Product product) {
        cartProductRepository.deleteByCartIdAndProductId(cart.getId(), product.getId());
    }

    public void deleteAllCartProducts(Cart cart) {
        cartProductRepository.deleteAllCartProductsByCartId(cart.getId());
    }
}
