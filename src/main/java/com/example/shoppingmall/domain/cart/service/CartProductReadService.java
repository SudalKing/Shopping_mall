package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.dto.CartProductDto;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartProductReadService {

    public List<CartProductDto> toDtoList(List<CartProduct> cartProducts){
        return cartProducts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CartProductDto toDto(CartProduct cartProduct){
        return new CartProductDto(
                cartProduct.getId(),
                cartProduct.getCart(),
                cartProduct.getProduct(),
                cartProduct.getCount(),
                cartProduct.getCreatedAt()
        );
    }
}
