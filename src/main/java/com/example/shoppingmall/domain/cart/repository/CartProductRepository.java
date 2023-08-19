package com.example.shoppingmall.domain.cart.repository;

import com.example.shoppingmall.domain.cart.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    CartProduct findCartProductByProductIdAndCartId(Long productId, Long cartId);

    @Transactional
    @Modifying
    @Query(value = "delete from cart_product where cart_id in :cartIds", nativeQuery = true)
    void deleteAllByCartId(@Param("cartIds") List<Long> cartIds);
}
