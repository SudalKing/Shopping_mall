package com.example.shoppingmall.domain.cart.repository;

import com.example.shoppingmall.domain.cart.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    Optional<CartProduct> findCartProductByProductIdAndCartId(Long productId, Long cartId);

    List<CartProduct> findCartProductsByCartIdAndEnabledTrue(Long cartId);

    CartProduct findCartProductByProductId(Long productId);

    void deleteByCartIdAndProductId(Long cartId, Long productId);

    @Query(value = "select product_id from cart_product where cart_id = :cartId", nativeQuery = true)
    List<Long> findProductIdsByCartId(Long cartId);

    @Query(value = "select cp.amount from cart_product as cp where cp.product_id = :productId", nativeQuery = true)
    int findAmountByProductId(Long productId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from cart_product where cart_id = :cartId", nativeQuery = true)
    void deleteAllCartProductsByCartId(@Param("cartId") Long cartId);
}
