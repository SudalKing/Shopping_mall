package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrdersRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderWriteService {
    private final OrdersRepository ordersRepository;

    @Transactional
    public Orders createOrder(final User user){
        var orders = Orders.builder()
                .userId(user.getId())
                .versionCount(getMinVersionCount(user))
                .orderStatusId(1L) // 기본 1(주문 완료)로 저장 -> 이후 배송 중, 배송 완료 등으로 바꿈
                .priceSum(0)
                .createdAt(LocalDateTime.now())
                .build();
        return ordersRepository.save(orders);
    }

    public void deleteOrder(final Long userId) {
        ordersRepository.deleteAllByUserId(userId);
    }

    public void updateOrderPriceSum(final Orders order, final int priceSum) {
        order.setPriceSum(priceSum);
    }


    private Long getMinVersionCount(final User user) {
        Long versionCount = ordersRepository.findOrderCountByUserId(user.getId());
        return versionCount + 1;
    }

}
