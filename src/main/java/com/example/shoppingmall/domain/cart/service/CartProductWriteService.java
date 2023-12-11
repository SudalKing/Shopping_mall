package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.exception.InvalidQuantityException;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.global.error.exception.EntityNotFoundException;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CartProductWriteService {
    private final CartProductRepository cartProductRepository;
    private final CartReadService cartReadService;

    @Transactional
    public void createCartProduct(final Cart cart, final Product product, final int amount){
        Optional<CartProduct> findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());

        if(findCartProduct.isEmpty()){
            CartProduct cartProduct = CartProduct.builder()
                    .cartId(cart.getId())
                    .productId(product.getId())
                    .amount(amount)
                    .createdAt(LocalDateTime.now())
                    .enabled(true)
                    .build();
            cartProductRepository.save(cartProduct);
        } else {findCartProduct.get().addCount(amount);}
    }

    @Transactional
    public void decreaseCartProductCount(final Cart cart, final Product product) {
        Optional<CartProduct> findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());

        if (findCartProduct.isPresent() && findCartProduct.get().getAmount() > 1) {
            findCartProduct.get().minusCount(1);
        } else {
            throw new InvalidQuantityException("Can not decrease quantity", ErrorCode.INVALID_QUANTITY);
        }
    }

    @Transactional
    public void increaseCartProductCount(final Cart cart, final Product product) {
        Optional<CartProduct> findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());

        if (findCartProduct.isPresent()) {
            findCartProduct.get().addCount(1);
        } else {
            throw new EntityNotFoundException("Can not find CartProduct");
        }
    }

    @Transactional
    public void deleteCartProduct(final User user, final List<Long> productIds) {
        Cart cart = cartReadService.getCartInfo(user.getId());

        for (Long productId: productIds) {
            cartProductRepository.deleteByCartIdAndProductId(cart.getId(), productId);
        }
    }

    public void deleteAllCartProducts(final Cart cart) {
        cartProductRepository.deleteAllCartProductsByCartId(cart.getId());
    }
}
