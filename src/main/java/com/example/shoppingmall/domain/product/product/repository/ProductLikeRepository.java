package com.example.shoppingmall.domain.product.product.repository;

import com.example.shoppingmall.domain.product.product.entity.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    int countAllByProductId(Long productId);

    @Query(value = "select product_id from product_like group by product_id order by count(id) desc limit 1", nativeQuery = true)
    Long findProductIdMostLike();

    @Query(value = "select count(id) from product_like group by product_id order by count(id) desc limit 1", nativeQuery = true)
    int findLikeCountMostLike();

    @Query(value = "select count(id) from product_like where product_id = :productId group by product_id", nativeQuery = true)
    int findLikeCountByProductId(Long productId);

    Optional<ProductLike> findByUserIdAndProductId(Long userId, Long productId);
}