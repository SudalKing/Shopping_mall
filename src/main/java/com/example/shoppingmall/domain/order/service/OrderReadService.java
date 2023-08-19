package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.dto.OrderDto;
import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrdersRepository;
import com.example.shoppingmall.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderReadService {
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

//    public List<OrderDto> getAllOrders(Long userId){
//        return orderRepository.findAllByUserId(userId)
//                .stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
    public OrderDto getAllOrders(Long userId){
        var orders = ordersRepository.findOrderByUserId(userId);
        return toDto(orders);
    }

    public OrderDto getCurrentOrder(Long orderId, Long cartId){
        var orders = ordersRepository.findOrdersByIdAndCartId(orderId, cartId);
        return toDto(orders);
        // 1. OrdersId로 찾는다?
        // 2. Orders의 주문 상태를 바꾼다
    }

    public int getTotalPrice(Orders order) {
        int totalPrice = 0;
        var orderProducts = order.getOrderProducts();

        for (OrderProduct orderProduct: orderProducts) {
            var product = productRepository.findProductById(orderProduct.getProductId());
            totalPrice += orderProduct.getCount() * product.getPrice();
        }

        return totalPrice;
    }

    public OrderDto toDto(Orders order){
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getOrderStatusId(),
                order.getCreatedAt(),
                getTotalPrice(order)
        );
    }
}
