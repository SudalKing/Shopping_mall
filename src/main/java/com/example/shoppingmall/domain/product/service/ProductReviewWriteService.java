package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.req.ProductReviewRequest;
import com.example.shoppingmall.domain.product.entity.ProductReview;
import com.example.shoppingmall.domain.product.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductReviewWriteService {
    private final ProductReviewRepository productReviewRepository;

    public ProductReview createProductReview(Long userId, ProductReviewRequest productReviewRequest){
        var productReview = ProductReview.builder()
                .productId(productReviewRequest.getProductId())
                .userId(userId)
                .contents(productReviewRequest.getContents())
                .build();
        return productReviewRepository.save(productReview);
    }

    public void deleteProductReview(Long reviewId, Long userId){
        var productReview = productReviewRepository.findById(reviewId).orElseThrow();
        if(productReview.getUserId().equals(userId)) productReviewRepository.deleteById(reviewId);
        else throw new RuntimeException();
    }
}
