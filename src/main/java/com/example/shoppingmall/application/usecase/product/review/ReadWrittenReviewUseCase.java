package com.example.shoppingmall.application.usecase.product.review;

import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.review.dto.res.ProductReviewWrittenResponse;
import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.service.ProductReviewReadService;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReadWrittenReviewUseCase {
    private final ProductReadService productReadService;
    private final ProductReviewReadService productReviewReadService;

    @Transactional
    public List<ProductReviewWrittenResponse> execute(User user) {
        List<ProductReviewWrittenResponse> responses = new ArrayList<>();

        List<ProductReview> reviews = productReviewReadService.getAllReviewsByUserId(user.getId());

        for (ProductReview review : reviews) {
            Product product = productReadService.getProductEntity(review.getProductId());
            Map<String, String> clothesInfo = productReadService.getClothesSizeAndColor(product);

            ProductReviewWrittenResponse response = ProductReviewWrittenResponse.builder()
                    .reviewId(review.getId())
                    .orderId(review.getOrderId())
                    .productId(review.getProductId())
                    .name(product.getName())
                    .color(clothesInfo.get("color"))
                    .size(clothesInfo.get("size"))
//                    .imageUrl(productReadService.getUrls(product.getId()))
                    .imageUrl("URL")
                    .content(review.getContents())
                    .rating(review.getRating())
                    .createdAt(review.getCreatedAt())
                    .build();
            responses.add(response);
        }

        return responses;
    }
}
