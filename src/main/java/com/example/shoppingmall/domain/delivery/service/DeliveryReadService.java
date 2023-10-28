package com.example.shoppingmall.domain.delivery.service;

import com.example.shoppingmall.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveryReadService {
    private final DeliveryRepository deliveryRepository;

}
