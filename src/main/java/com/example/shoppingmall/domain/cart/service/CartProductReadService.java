package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.dto.CartProductDto;
import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartProductReadService {
    private final CartProductRepository cartProductRepository;

    public List<CartProductDto> getCartProductsByCart(Cart cart){
        return toDtoList(cartProductRepository.findCartProductsByCartIdAndEnabledTrue(cart.getId()));
    }

    public List<CartProduct> getCartProductsEntityByCart(Cart cart){
        return cartProductRepository.findCartProductsByCartIdAndEnabledTrue(cart.getId());
    }

    public List<CartProductDto> toDtoList(List<CartProduct> cartProducts){
        return cartProducts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CartProductDto toDto(CartProduct cartProduct){
        return new CartProductDto(
                cartProduct.getId(),
                cartProduct.getCartId(),
                cartProduct.getProductId(),
                cartProduct.getCount(),
                cartProduct.getCreatedAt(),
                cartProduct.isEnabled()
        );
    }
}
