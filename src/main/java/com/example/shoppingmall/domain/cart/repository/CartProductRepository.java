package com.example.shoppingmall.domain.cart.repository;

import com.example.shoppingmall.domain.cart.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    CartProduct findCartProductByProductIdAndCartId(Long productId, Long cartId);
}
