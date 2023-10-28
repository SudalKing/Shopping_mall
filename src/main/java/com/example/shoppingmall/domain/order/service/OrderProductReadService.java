package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderProductReadService {
    private final OrderProductRepository orderProductRepository;

    public Integer getPriceSum(Orders orders) {
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(orders.getId());

        int priceSum = 0;

        for (OrderProduct orderProduct: orderProducts) {
            priceSum += orderProduct.getCount() *
                    orderProductRepository.findPriceByProductId(orderProduct.getProductId());
        }

        return priceSum;
    }
}
