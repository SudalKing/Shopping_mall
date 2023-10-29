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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProductReviewWriteService {
    private final ProductReviewRepository productReviewRepository;
    private final OrderProductReadService orderProductReadService;

    @Transactional
    public ProductReview createProductReview(User user, ProductReviewRequest productReviewRequest){
        var productReview = ProductReview.builder()
                .orderId(productReviewRequest.getOrderId())
                .productId(productReviewRequest.getProductId())
                .userId(user.getId())
                .content(productReviewRequest.getContent())
                .rating(productReviewRequest.getRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        ProductReview savedReview = productReviewRepository.save(productReview);

        OrderProduct orderProduct = orderProductReadService.
                getOrderProductByOrderIdAndProductId(savedReview.getOrderId(), savedReview.getProductId());

        orderProduct.setReviewed();

        return savedReview;
    }

    public void deleteProductReview(Long reviewId, Long userId){
        var productReview = productReviewRepository.findById(reviewId).orElseThrow();
        if(productReview.getUserId().equals(userId)) productReviewRepository.deleteById(reviewId);
        else throw new RuntimeException();
    }

    @Transactional
    public void updateReview(Long reviewId, Map<String, Object> updates) {
        ProductReview productReview = productReviewRepository.findProductReviewById(reviewId);

        if (updates.containsKey("content")) {
            String content = updates.get("content").toString();
            productReview.updateContent(content);
        }

        if (updates.containsKey("rating")) {
            Integer rating = (Integer) updates.get("rating");
            productReview.updateRating(rating);
        }
    }

}
