package com.example.shoppingmall.domain.delivery.service;

import com.example.shoppingmall.domain.delivery.entity.Delivery;
import com.example.shoppingmall.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveryWriteService {
    private final DeliveryRepository deliveryRepository;

    public Long createDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery).getId();
    }
}
