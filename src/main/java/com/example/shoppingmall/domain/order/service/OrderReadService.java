package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.dto.OrderDto;
import com.example.shoppingmall.domain.order.entity.Order;
import com.example.shoppingmall.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderReadService {
    private final OrderRepository orderRepository;

    public List<OrderDto> getAllOrder(Long userId){
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OrderDto toDto(Order order){
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getCartId(),
                order.getOrderStatusId(),
                order.getTotalPrice(),
                order.getCreatedAt()
        );
    }
}
