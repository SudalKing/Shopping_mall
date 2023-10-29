package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.dto.OrderDto;
import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrderProductRepository;
import com.example.shoppingmall.domain.order.repository.OrdersRepository;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderReadService {
    private final OrdersRepository ordersRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    public List<Orders> getAllOrdersEntity(User user) {
        return ordersRepository.findAllByUserId(user.getId());
    }

    public List<OrderDto> getAllOrders(User user){
        return ordersRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OrderDto getCurrentOrder(Long orderId){
        var orders = ordersRepository.findOrdersById(orderId);
        return toDto(orders);
    }

    public OrderDto toDto(Orders order){
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getOrderStatusId(),
                order.getCreatedAt(),
                order.getVersionCount(),
                getTotalPrice(order)
        );
    }

    private int getTotalPrice(Orders order) {
        int totalPrice = 0;
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(order.getId());

        for (OrderProduct orderProduct: orderProducts) {
            var product = productRepository.findProductById(orderProduct.getProductId());
            totalPrice += orderProduct.getCount() * product.getPrice();
        }

        return totalPrice;
    }
}
