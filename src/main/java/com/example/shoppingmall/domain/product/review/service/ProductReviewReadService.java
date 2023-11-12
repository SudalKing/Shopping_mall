package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.awsS3.service.ProductReviewImageReadService;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.review.dto.res.ReviewListResponse;
import com.example.shoppingmall.domain.product.review.dto.res.ReviewStatsResponse;
import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewRepository;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductReviewReadService {
    private final ProductReviewRepository productReviewRepository;

    private final ProductReviewImageReadService productReviewImageReadService;
    private final UserReadService userReadService;
    private final ProductReadService productReadService;
    private final ProductReviewLikeReadService productReviewLikeReadService;

    public List<ProductReview> getAllReviewsByUserIdOrderByCreatedAt(Long userId) {
        return productReviewRepository.findAllByUserIdOrderByCreatedAt(userId);
    }

    public ProductReview getReviewByReviewId(Long reviewId) {
        return productReviewRepository.findProductReviewById(reviewId);
    }

    /**
     * 상품 id를 통해 상품의 리뷰 통계 조회
     */
    public ReviewStatsResponse getReviewStatsByProductId(final Long productId) {
        return ReviewStatsResponse.builder()
                .totalCount(getTotalReviewCount(productId))
                .averageRating(getAverageRating(productId))
                .proportion(getProportion(productId))
                .build();
    }

    /**
     * 리뷰 목록 조회
     */
    public PageCursor<ReviewListResponse> getProductReviewsByCursor(Number key, int size, Long sortId, Long productId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var productReviews = findAllReview(cursorRequest, sortId, productId);

        return getProductResponsePageCursor(cursorRequest, productReviews, sortId);
    }

    /**
     * 리뷰 사진 목록 조회
     */
    public PageCursor<ReviewListResponse> getReviewImagesByCursor(Number key, int size, Long productId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var productReviews = findAllReviewImage(cursorRequest, productId);

        return getReviewImageResponsePageCursor(cursorRequest, productReviews);
    }


    // ================================================= 로직 모음 ==========================================
    /**
     * 리뷰 통계 조회 메소드 로직
     */
    public void getReviewStatsByProductIdLogics(Long productId) {
        getTotalReviewCount(productId);
        getAverageRating(productId);
        getProportion(productId);
    }

    /**
     * 리뷰 목록 조회 메소드 로직
     */
    public void getProductReviewsByCursorLogics() {
//        getProductResponsePageCursor();
//        findAllReview();
//        getNextKey();
//        getLikeCountNextKey();
    }

    // ====================================================================================================
    // ================================================== 메소드 ============================================
    private Integer getTotalReviewCount(final Long productId) {
        return productReviewRepository.countAllByProductId(productId);
    }

    private Double getAverageRating(final Long productId) {
        return Math.round(productReviewRepository.sumTotalReviewByProductId(productId).doubleValue()
                / getTotalReviewCount(productId) * 10.0) / 10.0;
    }

    private List<Integer> getProportion(final Long productId) {
        List<Integer> proportion = new ArrayList<>();
        Integer total = 0;

        for (int i = 0; i < 5; i++) {
            total += productReviewRepository.countByProductIdAndRating(productId, i + 1);
        }

        for (int i = 0; i < 5; i++) {
            var rCount = productReviewRepository.countByProductIdAndRating(productId, i + 1) * 100 / total;
            proportion.add(rCount); // 비울러
        }
        Collections.reverse(proportion);

        return proportion;
    }

    private PageCursor<ReviewListResponse> getProductResponsePageCursor(CursorRequest cursorRequest, List<ProductReview> productReviews, Long sortId) throws Exception {
        var productReviewList = productReviews.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());

        if (sortId == 0L) {
            var nextKey = getNextKey(productReviews);
            return new PageCursor<>(cursorRequest.next(nextKey), productReviewList);
        } else if (sortId == 1L) {
            var nextKey = getLikeCountNextKey(productReviewList);
            return new PageCursor<>(cursorRequest.next(nextKey), productReviewList);
        } else {
            throw new Exception("Wrong SortId!!");
        }
    }

    private List<ProductReview> findAllReview(CursorRequest cursorRequest, Long sortId, Long productId) throws Exception {
        if (sortId == 0L) {
            if (cursorRequest.hasKey()) {
                return productReviewRepository.findAllByProductIdByCursorOrderByIdDescHasKey(cursorRequest.getKey().longValue(), cursorRequest.getSize(), productId);
            } else {
                return productReviewRepository.findAllByProductIdByCursorOrderByIdDescNoKey(cursorRequest.getSize(), productId);
            }
        } else if (sortId == 1L) {
            if (cursorRequest.hasKey()) {
                return productReviewRepository.findAllByProductIdByCursorOrderByLikeDescHasKey(cursorRequest.getKey().intValue(), cursorRequest.getSize(), productId);
            } else {
                return productReviewRepository.findAllByProductIdByCursorOrderByLikeDescNoKey(cursorRequest.getSize(), productId);
            }
        } else {
            throw new Exception("Wrong sortId");
        }
    }

    private PageCursor<ReviewListResponse> getReviewImageResponsePageCursor(CursorRequest cursorRequest, List<ProductReview> productReviews) {
        var productReviewList = productReviews.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());

        var nextKey = getNextKey(productReviews);
        return new PageCursor<>(cursorRequest.next(nextKey), productReviewList);
    }

    private List<ProductReview> findAllReviewImage(CursorRequest cursorRequest, Long productId) throws Exception {
        if (cursorRequest.hasKey()) {
            return productReviewRepository.findAllExistImageByProductIdByCursorOrderByIdDescHasKey(cursorRequest.getKey().longValue(), cursorRequest.getSize(), productId);
        } else {
            return productReviewRepository.findAllExistImageByProductIdByCursorOrderByIdDescNoKey(cursorRequest.getSize(), productId);

        }
    }

    private Long getNextKey(List<ProductReview> productReviews){
        return productReviews.stream()
                .mapToLong(ProductReview::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY_LONG);
    }
    private Integer getLikeCountNextKey(List<ReviewListResponse> reviewListResponses){
        return reviewListResponses.stream()
                .mapToInt(ReviewListResponse::getReviewLikeCount)
                .min()
                .orElse(CursorRequest.NONE_KEY_INTEGER);
    }


    private ReviewListResponse toListResponse(ProductReview productReview) {
        User user = userReadService.getUserEntity(productReview.getUserId());
        Map<String, String> clothesInfo = productReadService.getClothesSizeAndColor(productReadService.getProductEntity(productReview.getProductId()));

        return ReviewListResponse.builder()
                .reviewId(productReview.getId())
                .userName(user.getName())
                .color(clothesInfo.get("color"))
                .size(clothesInfo.get("size"))
                .rating(productReview.getRating())
                .reviewImageUrl(productReviewImageReadService.getUrl(productReview.getId()))
                .reviewLikeCount(productReviewLikeReadService.getReviewLikeCount(productReview.getId()))
                .createdAt(productReview.getCreatedAt())
                .build();
    }
}
