package com.example.shoppingmall.scheduled;

import com.example.shoppingmall.domain.product.best.service.BestWriteService;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.product.service.ProductWriteService;
import com.example.shoppingmall.domain.product.product_like.service.ProductLikeReadService;
import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.repository.ProductReviewRepository;
import com.example.shoppingmall.domain.product.review.service.ProductReviewWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SchedulerService {

    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;

    private final ProductWriteService productWriteService;
    private final ProductLikeReadService productLikeReadService;
    private final ProductReviewWriteService productReviewWriteService;
    private final BestWriteService bestWriteService;

    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Seoul")
    public void reviewLikeScoreScheduler() {
        log.info("리뷰 좋아요 점수 계산 스케쥴러 실행");
        List<ProductReview> productReviewList = productReviewRepository.findAll();

        for (ProductReview productReview: productReviewList) {
            productReviewWriteService.updateReviewLikeScore(productReview.getId());
        }
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Seoul")
    public void bestScoreScheduler() {
        log.info("베스트 상품 계산 스케쥴러 실행");
        List<Product> productList = productRepository.findAll();

        for (Product product: productList) {
            bestWriteService.updateTotalLike(product.getId());
            bestWriteService.updateScore(product.getId());
        }
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Seoul")
    public void productLikeScheduler() {
        log.info("상품 좋아요 집계 스케쥴러 실행");
        List<Product> productList = productRepository.findAll();

        for (Product product: productList) {
            product.setLikeCount(productLikeReadService.getProductLikeCount(product.getId()));
        }
    }
}
