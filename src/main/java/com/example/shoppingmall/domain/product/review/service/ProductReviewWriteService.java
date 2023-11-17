package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.order.entity.OrderProduct;
import com.example.shoppingmall.domain.order.service.OrderProductReadService;
import com.example.shoppingmall.domain.product.review.dto.req.ProductReviewRequest;
import com.example.shoppingmall.domain.product.review.dto.req.UpdateProductReviewRequest;
import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.entity.ReviewLikeScore;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewLikeRepository;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewRepository;
import com.example.shoppingmall.domain.product.review.repository.ReviewLikeScoreRepository;
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
    private final ProductReviewLikeRepository productReviewLikeRepository;
    private final ReviewLikeScoreRepository reviewLikeScoreRepository;

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

        createReviewLikeScore(savedReview.getId());

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

    public void deleteAllReview(Long userId) {
        productReviewRepository.deleteAllByUserId(userId);
        productReviewRepository.deleteReviewImagesByUserId(userId);
        productReviewLikeRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void updateReview(Long reviewId, UpdateProductReviewRequest updates) {
        ProductReview productReview = productReviewRepository.findProductReviewById(reviewId);

        if (updates.getContent() != null) {
            productReview.updateContent(updates.getContent());
        }

        if (updates.getRating() != null) {
            productReview.updateRating(updates.getRating());
        }
    }

    public void createReviewLikeScore(Long productReviewId) {
        ReviewLikeScore reviewLikeScore = ReviewLikeScore.builder()
                .reviewId(productReviewId)
                .reviewScore(getReviewScore(productReviewId))
                .updatedAt(LocalDateTime.now())
                .build();
        reviewLikeScoreRepository.save(reviewLikeScore);
    }

    @Transactional
    public void updateReviewLikeScore(Long reviewId) {
        ReviewLikeScore reviewLikeScore = reviewLikeScoreRepository.findReviewLikeScoreByReviewId(reviewId).orElse(null);

        if (reviewLikeScore == null) {
            createReviewLikeScore(reviewId);
        } else {
            reviewLikeScore.setReviewScore(getReviewScore(reviewId));
        }
    }

    public void deleteReviewLikeScore(Long reviewId) {
        reviewLikeScoreRepository.deleteByReviewId(reviewId);
    }

    private Double getReviewScore(Long reviewId) {
        Integer likeCount = productReviewLikeRepository.countAllByReviewId(reviewId);
        return (likeCount * 0.99999 + reviewId * 0.00001);
    }

}
