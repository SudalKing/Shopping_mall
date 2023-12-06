package com.example.shoppingmall.application.usecase.product.review;

import com.example.shoppingmall.domain.awsS3.service.ProductImageReadService;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.service.OrderProductReadService;
import com.example.shoppingmall.domain.order.service.OrderReadService;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.product.service.ProductUtilService;
import com.example.shoppingmall.domain.product.review.dto.res.ProductReviewWriteableResponse;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReadWriteableReviewUseCase {
    private final ProductReadService productReadService;
    private final ProductImageReadService productImageReadService;
    private final OrderReadService orderReadService;
    private final OrderProductReadService orderProductReadService;

    private final ProductUtilService productUtilService;

    public List<ProductReviewWriteableResponse> execute(User user) {
        List<ProductReviewWriteableResponse> productReviewWriteableResponses = new ArrayList<>();
        List<Orders> orders = orderReadService.getAllOrdersEntity(user);

        for (Orders order: orders) {
            List<Long> productIds = orderProductReadService.getOrderProductIdsByOrderIdNotReviewed(order.getId());

            for (Long productId: productIds) {
                Product product = productReadService.getProductEntity(productId);
                Map<String, String> clothesInfo = productUtilService.getClothesInfo(product);
                ProductReviewWriteableResponse response = ProductReviewWriteableResponse.builder()
                        .productId(productId)
                        .orderId(order.getId())
                        .name(product.getName())
                        .color(clothesInfo.get("color"))
                        .size(clothesInfo.get("size"))
                        .imageUrl(productImageReadService.getUrl(productId))
                        .build();
                productReviewWriteableResponses.add(response);
            }
        }

        return productReviewWriteableResponses;
    }
}
