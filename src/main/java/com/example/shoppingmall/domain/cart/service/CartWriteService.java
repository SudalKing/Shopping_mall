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
        if(user.getCart() == null){
            var cart = Cart.builder()
                    .user(user)
                    .cartProducts(null)
                    .build();
            return cartRepository.save(cart);
        }else {
            return user.getCart();
        }
    }

    public void deleteCart(User user){
        cartRepository.deleteById(user.getCart().getId());
    }

}
