package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartReadService {
    private final CartRepository cartRepository;

    public Cart getCartInfo(final Long userId){
        return cartRepository.findCartByUserId(userId);
    }

}
