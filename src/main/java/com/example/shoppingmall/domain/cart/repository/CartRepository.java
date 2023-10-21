package com.example.shoppingmall.domain.cart.repository;

import com.example.shoppingmall.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
    Cart findCartByUserId(Long userId);
    void deleteByUserId(Long userId);
}
