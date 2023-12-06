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
    public Orders createOrder(User user){
        var orders = Orders.builder()
                .userId(user.getId())
                .versionCount(getMinVersionCount(user))
                .orderStatusId(1L) // 기본 1(주문 완료)로 저장 -> 이후 배송 중, 배송 완료 등으로 바꿈
                .priceSum(0)
                .createdAt(LocalDateTime.now())
                .build();
        return ordersRepository.save(orders);
    }

    public void deleteOrder(Long userId) {
        ordersRepository.deleteAllByUserId(userId);
    }

    public void updateOrderPriceSum(Orders order, int priceSum) {
        order.setPriceSum(priceSum);
    }



    // 1. userId 로 그룹화 한 후 행 개수 계산
    // 2. + 1 하여 반환
    private Long getMinVersionCount(User user) {
        Long versionCount = ordersRepository.findOrderCountByUserId(user.getId());
        return versionCount + 1;
    }

}
