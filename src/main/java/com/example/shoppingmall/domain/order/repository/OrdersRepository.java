package com.example.shoppingmall.domain.order.repository;

import com.example.shoppingmall.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserId(Long userId);
    Orders findOrderByUserId(Long userId);
    Orders findOrdersByIdAndCartId(Long id, Long cartId);
}
