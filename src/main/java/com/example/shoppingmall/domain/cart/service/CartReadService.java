package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.dto.CartDto;
import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartRepository;
import com.example.shoppingmall.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartReadService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartDto getCartInfo(Long userId){
        var cart = cartRepository.findByUserId(userId);
        return toDto(cart);
    }

    public CartDto toDto(Cart cart){
        return new CartDto(
                cart.getId(),
                cart.getUser().getId(),
                getTotalPrice(cart)
        );
    }

    public int getTotalPrice(Cart cart) {
        int totalPrice = 0;
        var cartProducts = cart.getCartProducts();

        for (CartProduct cartProduct: cartProducts) {
            var product = productRepository.findProductById(cartProduct.getProduct().getId());
            totalPrice += cartProduct.getCount() * product.getPrice();
        }

        return totalPrice;
    }
}
