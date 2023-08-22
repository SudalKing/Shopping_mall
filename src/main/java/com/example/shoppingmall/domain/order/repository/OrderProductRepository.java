package com.example.shoppingmall.domain.order.repository;

import com.example.shoppingmall.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
