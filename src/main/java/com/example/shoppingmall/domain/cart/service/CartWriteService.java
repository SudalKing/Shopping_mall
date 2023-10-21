package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.repository.CartRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartWriteService {
    private final CartRepository cartRepository;

    public Cart createCart(User user) {
        var findCart = cartRepository.findByUserId(user.getId());
        if(findCart.isPresent()){
            return findCart.get();
        }else {
            var cart = Cart.builder()
                    .userId(user.getId())
                    .enabled(true)
                    .build();
            return cartRepository.save(cart);}
    }

    public void deleteCart(User user){
        cartRepository.deleteByUserId(user.getId());
    }

}
