package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartProductWriteService {
    private final CartProductRepository cartProductRepository;
    private final CartReadService cartReadService;

    @Transactional
    public void createCartProduct(Cart cart, Product product, int amount){
        CartProduct findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());

        if(findCartProduct == null){
            CartProduct cartProduct = CartProduct.builder()
                    .cartId(cart.getId())
                    .productId(product.getId())
                    .amount(amount)
                    .createdAt(LocalDateTime.now())
                    .enabled(true)
                    .build();
            cartProductRepository.save(cartProduct);
        } else {findCartProduct.addCount(amount);}
    }

    @Transactional
    public void decreaseCartProductCount(Cart cart, Product product) throws Exception {
        CartProduct findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());

        if (findCartProduct.getAmount() > 1) {
            findCartProduct.minusCount(1);
        } else {
            throw new Exception("Can not minus!!");
        }
    }

    @Transactional
    public void increaseCartProductCount(Cart cart, Product product) throws Exception {
        CartProduct findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());
        findCartProduct.addCount(1);
    }

    @Transactional
    public void deleteCartProduct(User user, List<Long> productIds) {
        Cart cart = cartReadService.getCartInfo(user.getId());

        for (Long productId: productIds) {
            cartProductRepository.deleteByCartIdAndProductId(cart.getId(), productId);
        }
    }

    public void deleteAllCartProducts(Cart cart) {
        cartProductRepository.deleteAllCartProductsByCartId(cart.getId());
    }
}
