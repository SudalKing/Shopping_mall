package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.order.dto.res.OrderResponse;
import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrderProductRepository;
import com.example.shoppingmall.domain.order.repository.OrdersRepository;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.product.service.ProductUtilService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderReadService {
    private final OrdersRepository ordersRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductReadService productReadService;
    private final ProductUtilService productUtilService;

    public List<Orders> getAllOrdersEntity(User user) {
        return ordersRepository.findAllByUserId(user.getId());
    }


    public PageCursor<OrderResponse> getAllOrdersByCursor(Number key, int size, User user) {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var orderProductList = findAllOrderProduct(cursorRequest, user.getId());

        return getOrderResponseCursor(cursorRequest, orderProductList);
    }

    private List<OrderProduct> findAllOrderProduct(CursorRequest cursorRequest, Long userId) {
        if (cursorRequest.hasKey()) {
            return orderProductRepository.findAllByUserIdOrderByIdHasKey(cursorRequest.getKey().longValue(), userId, cursorRequest.getSize());
        } else {
            return orderProductRepository.findAllByUserIdOrderByIdNoKey(userId, cursorRequest.getSize());
        }
    }


    private OrderResponse toOrderResponse(OrderProduct orderProduct){
        Product product = productReadService.getProductEntity(orderProduct.getProductId());
        Map<String, String> clothesInfo = productUtilService.getClothesInfo(product);

        return OrderResponse.builder()
                .orderId(orderProduct.getOrderId())
                .productId(orderProduct.getProductId())
                .productName(product.getName())
                .color(clothesInfo.get("color"))
                .size(clothesInfo.get("size"))
                .price(product.getPrice())
                .discountPrice(productUtilService.getDiscountPrice(product))
                .amount(orderProduct.getCount())
                .status(getStatus(orderProduct))
                .imageUrl(productReadService.getUrl(product))
                .createdAt(ordersRepository.findCreatedAtById(orderProduct.getOrderId()))
                .build();
    }

    private Long getNextKey(List<OrderProduct> orderProductList){
        return orderProductList.stream()
                .mapToLong(OrderProduct::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY_LONG);
    }

    private PageCursor<OrderResponse> getOrderResponseCursor(CursorRequest cursorRequest, List<OrderProduct> orderProductList) {
        var orderResponseList = orderProductList.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());

        var nextKey = getNextKey(orderProductList);
        return new PageCursor<>(cursorRequest.next(nextKey), orderResponseList);
    }

    private String getStatus(OrderProduct orderProduct) {
        Long orderStatusId = orderProduct.getOrderStatusId();

        if (orderStatusId == 1L) {
            return "paymentPending";
        } else if (orderStatusId == 2L) {
            return "shippingIn";
        } else if (orderStatusId == 3L) {
            return "delivered";
        } else if (orderStatusId == 4L) {
            return "confirmedPurchase";
        } else if (orderStatusId == 5L){
            return "exchangeReturn";
        } else {
            return "";
        }
    }
}
