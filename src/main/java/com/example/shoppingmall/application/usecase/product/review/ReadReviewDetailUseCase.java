package com.example.shoppingmall.application.usecase.product.review;

import com.example.shoppingmall.domain.awsS3.service.ProductImageReadService;
import com.example.shoppingmall.domain.awsS3.service.ProductReviewImageReadService;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.product.service.ProductUtilService;
import com.example.shoppingmall.domain.product.review.dto.res.ProductReviewDetailResponse;
import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.service.ProductReviewReadService;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReadReviewDetailUseCase {
    private final ProductReviewReadService productReviewReadService;
    private final ProductReadService productReadService;
    private final ProductImageReadService productImageReadService;
    private final ProductReviewImageReadService productReviewImageReadService;

    private final ProductUtilService productUtilService;

    @Transactional
    public ProductReviewDetailResponse execute(User user, Long reviewId) {
        ProductReview productReview = productReviewReadService.getReviewByReviewId(reviewId);
        Product product = productReadService.getProductEntity(productReview.getProductId());
        Map<String, String> clothesInfo = productUtilService.getClothesInfo(product);

        return ProductReviewDetailResponse.builder()
                .reviewId(reviewId)
                .productId(product.getId())
                .content(productReview.getContent())
                .rating(productReview.getRating())
                .name(product.getName())
                .color(clothesInfo.get("color"))
                .size(clothesInfo.get("size"))
                .productImageUrl(productImageReadService.getUrl(product.getId()))
                .reviewImageUrl(productReviewImageReadService.getUrl(reviewId))
                .build();
    }
}
