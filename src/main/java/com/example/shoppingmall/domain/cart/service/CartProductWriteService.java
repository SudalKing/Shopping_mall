package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CartProductWriteService {
    private final CartProductRepository cartProductRepository;

    @Transactional
    public void createCartProduct(Cart cart, Product product, int count){
        var findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());

        if(findCartProduct == null){
            var cartProduct = CartProduct.builder()
                    .cart(cart)
                    .product(product)
                    .count(count)
                    .createdAt(LocalDateTime.now())
                    .build();
            cartProductRepository.save(cartProduct);
        } else {findCartProduct.addCount(count);}
    }
}
