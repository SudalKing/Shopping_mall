package com.example.shoppingmall.domain.order.repository;

import com.example.shoppingmall.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findAllByOrderId(Long orderId);
    OrderProduct findOrderProductByOrderIdAndProductId(Long orderId, Long productId);

    @Query(value = "select product_id from order_product where order_id = :orderId and reviewed = false", nativeQuery = true)
    List<Long> findAllProductIdsByOrderIdAndNotReviewed(Long orderId);

    @Query(value = "select product_id from order_product where order_id = :orderId and reviewed = true", nativeQuery = true)
    List<Long> findAllProductIdsByOrderIdAndReviewed(Long orderId);

    @Query(value = "select price from product where id = :productId", nativeQuery = true)
    Integer findPriceByProductId(Long productId);
}
