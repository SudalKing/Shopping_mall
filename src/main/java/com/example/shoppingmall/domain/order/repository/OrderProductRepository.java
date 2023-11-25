package com.example.shoppingmall.domain.order.repository;

import com.example.shoppingmall.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findAllByOrderId(Long orderId);
    OrderProduct findOrderProductByOrderIdAndProductId(Long orderId, Long productId);

    @Query(value = "select count(*) from order_product as op " +
            "left join orders as o on op.order_id = o.id " +
            "where o.user_id = :userId", nativeQuery = true)
    Integer countAllByOrdersCount(@Param("userId") Long userId);

    @Query(value = "select count(*) from order_product as op " +
            "left join orders as o on op.order_id = o.id " +
            "where o.user_id = :userId and op.order_status_id = :statusId", nativeQuery = true)
    Integer countAllByOrdersCountByStatus(@Param("userId") Long userId,
                                          @Param("statusId") Long orderStatusId);

    @Query(value = "select product_id from order_product where order_id = :orderId and reviewed = false", nativeQuery = true)
    List<Long> findAllProductIdsByOrderIdAndNotReviewed(Long orderId);

    @Query(value = "select product_id from order_product where order_id = :orderId and reviewed = true", nativeQuery = true)
    List<Long> findAllProductIdsByOrderIdAndReviewed(Long orderId);

    @Query(value = "select price from product where id = :productId", nativeQuery = true)
    Integer findPriceByProductId(Long productId);

    @Query(value = "delete from order_product " +
            "where order_id in (select id from orders where user_id = :userId)", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") Long userId);

    @Query(value = "select * from order_product as op " +
            "left join orders as o on op.order_id = o.id " +
            "where op.id < :id and o.user_id = :userId " +
            "order by op.id desc limit :size", nativeQuery = true)
    List<OrderProduct> findAllByUserIdOrderByIdHasKey(@Param("id") Long id,
                                                      @Param("userId") Long userId,
                                                      @Param("size") int size);

    @Query(value = "select * from order_product as op " +
            "left join orders as o on op.order_id = o.id " +
            "where o.user_id = :userId " +
            "order by op.id desc limit :size", nativeQuery = true)
    List<OrderProduct> findAllByUserIdOrderByIdNoKey(@Param("userId") Long userId,
                                                     @Param("size") int size);
}
