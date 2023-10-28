package com.example.shoppingmall.domain.delivery.repository;

import com.example.shoppingmall.domain.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
