package com.example.shoppingmall.domain.order.service;

import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.order.dto.ProductsInfo;
import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.repository.OrderProductRepository;
import com.example.shoppingmall.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

}
