package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.dto.ProductsInfo;
import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderProductWriteService {
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public OrderProduct createOrderProduct(ProductsInfo productsInfo, Orders orders){
        var orderProduct = OrderProduct.builder()
                .orderId(orders.getId())
                .productId(productsInfo.getId())
                .count(productsInfo.getAmount())
                .build();
        return orderProductRepository.save(orderProduct);
    }

    public void deleteOrderProduct(Long userId) {
        orderProductRepository.deleteAllByUserId(userId);
    }
}
