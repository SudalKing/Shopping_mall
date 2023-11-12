package com.example.shoppingmall.domain.order.repository;

import com.example.shoppingmall.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findOrdersById(Long orderId);
    List<Orders> findAllByUserId(Long userId);
    Orders findOrderByUserId(Long userId);

    @Query(value = "select count(*) from orders where user_id = :userId", nativeQuery = true)
    Long findOrderCountByUserId(@Param("userId") Long userId);

    @Query(value = "select * from orders where id < :id and user_id = :userId order by id desc limit :size", nativeQuery = true)
    List<Orders> findOrdersByUserIdHasKey(@Param("id") Long id, @Param("userId") Long userId, @Param("size") int size);

    @Query(value = "select * from orders where user_id = :userId order by id desc limit :size", nativeQuery = true)
    List<Orders> findOrdersByUserIdNoKey(@Param("userId") Long userId, @Param("size") int size);
}
