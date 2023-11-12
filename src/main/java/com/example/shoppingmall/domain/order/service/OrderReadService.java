package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.dto.OrderDto;
import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrderProductRepository;
import com.example.shoppingmall.domain.order.repository.OrdersRepository;
import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
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


    public PageCursor<OrderDto> getAllOrdersByCursor(Number key, int size, User user) {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var orders = findAll(cursorRequest, user.getId());
        return getOrderResponseCursor(cursorRequest, orders);
    }

    private List<Orders> findAll(CursorRequest cursorRequest, Long userId) {
        if (cursorRequest.hasKey()) {
            return ordersRepository.findOrdersByUserIdHasKey(
                    cursorRequest.getKey().longValue(),
                    userId,
                    cursorRequest.getSize());
        } else {
            return ordersRepository.findOrdersByUserIdNoKey(userId, cursorRequest.getSize());
        }
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

    private Long getNextKey(List<Orders> orders){
        return orders.stream()
                .mapToLong(Orders::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY_LONG);
    }

    private PageCursor<OrderDto> getOrderResponseCursor(CursorRequest cursorRequest, List<Orders> orders) {
        var orderDtoList = orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        var nextKey = getNextKey(orders);
        return new PageCursor<>(cursorRequest.next(nextKey), orderDtoList);
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
