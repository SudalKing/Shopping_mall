package com.example.shoppingmall.application.usecase;

import com.example.shoppingmall.domain.cart.service.CartProductWriteService;
import com.example.shoppingmall.domain.order.service.OrderWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateOrderByCartUseCase {
    private final OrderWriteService orderWriteService;
    private final CartProductWriteService cartProductWriteService;
}
