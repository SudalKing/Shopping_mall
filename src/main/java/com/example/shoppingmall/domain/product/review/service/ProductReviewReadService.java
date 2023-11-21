package com.example.shoppingmall.domain.product.review.service;

import com.example.shoppingmall.domain.awsS3.service.ProductReviewImageReadService;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.review.dto.res.RecentReviewResponse;
import com.example.shoppingmall.domain.product.review.dto.res.ReviewListResponse;
import com.example.shoppingmall.domain.product.review.dto.res.ReviewStatsResponse;
import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewRepository;
import com.example.shoppingmall.domain.product.review.repository.ReviewLikeScoreRepository;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.example.shoppingmall.global.error.exception.InvalidValueException;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductReviewReadService {
    private final ProductReviewRepository productReviewRepository;
    private final ReviewLikeScoreRepository reviewLikeScoreRepository;

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
                .photoReviewCount(productReviewImageReadService.getPhotoReviewCount(productId))
                .build();
    }

    /**
     * 리뷰 목록 조회
     */
    public PageCursor<ReviewListResponse> getProductReviewsByCursor(Number key, int size, Long sortId, Long productId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        List<ProductReview> productReviewList = findAllReviewByProductId(cursorRequest, sortId, productId);

        return getProductResponsePageCursor(cursorRequest, productReviewList, sortId);
    }

    /**
     * 최근 리뷰 조회
     */
    public PageCursor<RecentReviewResponse> getRecentReviewsByCursor(Number key, int size) {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        List<ProductReview> productReviewList = findAllReview(cursorRequest);

        return getRecentReviewCursor(cursorRequest, productReviewList);
    }

    /**
     * 리뷰 사진 목록 조회
     */
    public PageCursor<ReviewListResponse> getReviewImagesByCursor(Number key, int size, Long productId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var productReviews = findAllReviewImage(cursorRequest, productId);

        return getReviewImageResponsePageCursor(cursorRequest, productReviews);
    }

    /**
     * 좋아요 검증
     */
    public void validatePrincipalLike(Principal principal, List<ReviewListResponse> cursorBody){
        if (principal != null) {
            Optional<User> userOptional = userReadService.getUserPrincipal(principal.getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                updateLikeTrue(user, cursorBody);
            }
        }
    }

    public void validatePrincipalLikeRecentReview(Principal principal, List<RecentReviewResponse> cursorBody){
        if (principal != null) {
            Optional<User> userOptional = userReadService.getUserPrincipal(principal.getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                updateLikeTrueRecentReview(user, cursorBody);
            }
        }
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
        if (productReviewRepository.sumTotalReviewByProductId(productId) == null) {
            return 0.;
        }

        return Math.round(productReviewRepository.sumTotalReviewByProductId(productId).doubleValue()
                / getTotalReviewCount(productId) * 10.0) / 10.0;
    }

    private List<Integer> getProportion(final Long productId) {
        List<Integer> proportion = new ArrayList<>();
        Integer total = 0;

        for (int i = 0; i < 5; i++) {
            total += productReviewRepository.countByProductIdAndRating(productId, i + 1);
        }

        if (total == 0) {
            return List.of(0, 0, 0, 0, 0);
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
            throw new InvalidValueException("Wrong SortId", ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private PageCursor<ReviewListResponse> getReviewImageResponsePageCursor(CursorRequest cursorRequest, List<ProductReview> productReviews) {
        var productReviewList = productReviews.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());

        var nextKey = getNextKey(productReviews);
        return new PageCursor<>(cursorRequest.next(nextKey), productReviewList);
    }

    private PageCursor<RecentReviewResponse> getRecentReviewCursor(CursorRequest cursorRequest, List<ProductReview> productReviewList) {
        List<RecentReviewResponse> recentReviewResponses = productReviewList.stream()
                .map(this::toRecentResponse)
                .collect(Collectors.toList());

        Long nextKey = getNextKey(productReviewList);

        return new PageCursor<>(cursorRequest.next(nextKey), recentReviewResponses);
    }

    private List<ProductReview> findAllReviewByProductId(CursorRequest cursorRequest, Long sortId, Long productId) throws Exception {
        if (sortId == 0L) {
            if (cursorRequest.hasKey()) {
                return productReviewRepository.findAllByProductIdByCursorOrderByIdDescHasKey(cursorRequest.getKey().longValue(), cursorRequest.getSize(), productId);
            } else {
                return productReviewRepository.findAllByProductIdByCursorOrderByIdDescNoKey(cursorRequest.getSize(), productId);
            }
        } else if (sortId == 1L) {
            if (cursorRequest.hasKey()) {
                return productReviewRepository.findAllByProductIdByCursorOrderByLikeDescHasKey(cursorRequest.getKey().doubleValue(), cursorRequest.getSize(), productId);
            } else {
                return productReviewRepository.findAllByProductIdByCursorOrderByLikeDescNoKey(cursorRequest.getSize(), productId);
            }
        } else {
            throw new InvalidValueException("Wrong SortId", ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private List<ProductReview> findAllReview(CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return productReviewRepository.findAllRecentReviewHasKey(cursorRequest.getKey().longValue(), cursorRequest.getSize());
        } else {
            return productReviewRepository.findAllRecentReviewNoKey(cursorRequest.getSize());
        }
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
    private Double getLikeCountNextKey(List<ReviewListResponse> reviewListResponses){
        return reviewListResponses.stream()
                .mapToDouble(ReviewListResponse::getReviewScore)
                .min()
                .orElse(CursorRequest.NONE_KEY_INTEGER);
    }

    private void updateLikeTrue(User user, List<ReviewListResponse> reviewListResponses) {
        reviewListResponses.forEach(
                reviewListResponse -> {
                    if (productReviewLikeReadService.getByUserIdAndReviewId(user.getId(), reviewListResponse.getReviewId()).isPresent()) {
                        reviewListResponse.setLiked();
                    }
                }
        );
    }

    private void updateLikeTrueRecentReview(User user, List<RecentReviewResponse> recentReviewResponses) {
        recentReviewResponses.forEach(
                recentReviewResponse -> {
                    if (productReviewLikeReadService.getByUserIdAndReviewId(user.getId(), recentReviewResponse.getReviewId()).isPresent()) {
                        recentReviewResponse.setLiked();
                    }
                }
        );
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
                .content(productReview.getContent())
                .reviewImageUrl(productReviewImageReadService.getUrl(productReview.getId()))
                .reviewLikeCount(productReviewLikeReadService.getReviewLikeCount(productReview.getId()))
                .reviewScore(getReviewScore(productReview.getId()))
                .createdAt(productReview.getCreatedAt())
                .build();
    }

    private RecentReviewResponse toRecentResponse(ProductReview productReview) {
        User user = userReadService.getUserEntity(productReview.getUserId());
        Product product = productReadService.getProductEntity(productReview.getProductId());

        return RecentReviewResponse.builder()
                .reviewId(productReview.getId())
                .userName(user.getName())
                .productId(productReview.getProductId())
                .productName(product.getName())
                .productImageUrl(productReadService.getUrl(product))
                .price(product.getPrice())
                .content(productReview.getContent())
                .reviewImageUrl(productReviewImageReadService.getUrl(product.getId()))
                .reviewLikeCount(productReviewLikeReadService.getReviewLikeCount(productReview.getId()))
                .liked(false)
                .createdAt(productReview.getCreatedAt())
                .build();
    }

    private Double getReviewScore(Long reviewId) {
        return reviewLikeScoreRepository.findReviewLikeScoreByReviewId(reviewId)
                .get().getReviewScore();
    }
}
