package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OrderProductWriteService {
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public OrderProduct createOrderProduct(CartProduct cartProduct, Orders orders){
        var orderProduct = OrderProduct.builder()
                .orderId(orders.getId())
                .productId(cartProduct.getProductId())
                .count(cartProduct.getCount())
                .build();
        return orderProductRepository.save(orderProduct);
    }
}
