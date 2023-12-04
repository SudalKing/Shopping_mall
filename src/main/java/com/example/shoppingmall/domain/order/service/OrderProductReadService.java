package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.dto.res.OrderStatsResponse;
import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrderProductRepository;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderProductReadService {
    private final OrderProductRepository orderProductRepository;

    private final ProductReadService productReadService;

    public List<Long> getOrderProductIdsByOrderIdNotReviewed(Long orderId) {
        return orderProductRepository.findAllProductIdsByOrderIdAndNotReviewed(orderId);
    }

    public List<Long> getOrderProductIdsByOrderIdReviewed(Long orderId) {
        return orderProductRepository.findAllProductIdsByOrderIdAndReviewed(orderId);
    }

    public OrderProduct getOrderProductByOrderIdAndProductId(Long orderId, Long productId) {
        return orderProductRepository.findOrderProductByOrderIdAndProductId(orderId, productId);
    }

    public OrderStatsResponse getOrderStats(User user) {
        return OrderStatsResponse.builder()
                .totalOrdersCount(orderProductRepository.countAllByOrdersCount(user.getId()))
                .paymentPendingCount(orderProductRepository.countAllByOrdersCountByStatus(user.getId(), 1L))
                .shippingInCount(orderProductRepository.countAllByOrdersCountByStatus(user.getId(), 2L))
                .deliveredCount(orderProductRepository.countAllByOrdersCountByStatus(user.getId(), 3L))
                .confirmedPurchaseCount(orderProductRepository.countAllByOrdersCountByStatus(user.getId(), 4L))
                .exchangeReturnCount(orderProductRepository.countAllByOrdersCountByStatus(user.getId(), 5L))
                .build();
    }


    public Integer getPriceSum(Orders orders) {
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(orders.getId());

        int priceSum = 0;

        for (OrderProduct orderProduct: orderProducts) {
            priceSum += orderProduct.getCount() *
                    productReadService.getProductEntity(orderProduct.getProductId()).getPrice();
        }

        return priceSum;
    }
}
