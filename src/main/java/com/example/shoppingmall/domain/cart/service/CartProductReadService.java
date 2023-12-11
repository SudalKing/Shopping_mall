package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CartProductReadService {
    private final CartProductRepository cartProductRepository;


    public List<CartProduct> getCartProductsEntityByCart(final Cart cart){
        return cartProductRepository.findCartProductsByCartIdAndEnabledTrue(cart.getId());
    }

    public List<Long> getProductIdsInCart(final Cart cart) {
        return cartProductRepository.findProductIdsByCartId(cart.getId());
    }

    public LocalDateTime getCreatedAt(final Long productId) {
        return cartProductRepository.findCartProductByProductId(productId).getCreatedAt();
    }

    public int getAmount(final Long productId) {
        return cartProductRepository.findAmountByProductId(productId);
    }
}
