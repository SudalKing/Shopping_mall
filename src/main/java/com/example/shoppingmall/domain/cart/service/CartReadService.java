package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.dto.CartDto;
import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.cart.repository.CartRepository;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartReadService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;

    public Cart getCartInfo(Long userId){
        return cartRepository.findCartByUserId(userId);
    }

    public CartDto toDto(Cart cart){
        return new CartDto(
                cart.getId(),
                cart.getUserId(),
                getTotalPrice(cart)
        );
    }

    public int getTotalPrice(Cart cart) {
        int totalPrice = 0;
        List<CartProduct> cartProducts = cartProductRepository.findCartProductsByCartIdAndEnabledTrue(cart.getId());

        for (CartProduct cartProduct: cartProducts) {
            var product = productRepository.findProductById(cartProduct.getProductId());
            totalPrice += cartProduct.getAmount() * product.getPrice();
        }

        return totalPrice;
    }
}
