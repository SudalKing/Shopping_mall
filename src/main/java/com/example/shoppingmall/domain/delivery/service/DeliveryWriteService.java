package com.example.shoppingmall.domain.delivery.service;

import com.example.shoppingmall.domain.delivery.entity.Delivery;
import com.example.shoppingmall.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class DeliveryWriteService {
    private final DeliveryRepository deliveryRepository;

    public Long createDelivery(final Delivery delivery) {
        return deliveryRepository.save(delivery).getId();
    }
}
