package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.service.OrderProductReadService;
import com.example.shoppingmall.domain.product.review.dto.req.ProductReviewRequest;
import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ProductReviewWriteService {
    private final ProductReviewRepository productReviewRepository;
    private final OrderProductReadService orderProductReadService;

    @Transactional
    public void createProductReview(User user, ProductReviewRequest productReviewRequest){
        var productReview = ProductReview.builder()
                .orderId(productReviewRequest.getOrderId())
                .productId(productReviewRequest.getProductId())
                .userId(user.getId())
                .contents(productReviewRequest.getContents())
                .rating(productReviewRequest.getRating())
                .imageUrl(productReviewRequest.getImageUrl())
                .createdAt(LocalDateTime.now())
                .build();
        ProductReview savedReview = productReviewRepository.save(productReview);

        OrderProduct orderProduct = orderProductReadService.
                getOrderProductByOrderIdAndProductId(savedReview.getOrderId(), savedReview.getProductId());

        orderProduct.setReviewed();
    }

    public void deleteProductReview(Long reviewId, Long userId){
        var productReview = productReviewRepository.findById(reviewId).orElseThrow();
        if(productReview.getUserId().equals(userId)) productReviewRepository.deleteById(reviewId);
        else throw new RuntimeException();
    }
}
