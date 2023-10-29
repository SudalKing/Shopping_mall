package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.product.review.ReadWriteableReviewUseCase;
import com.example.shoppingmall.application.usecase.product.review.ReadWrittenReviewUseCase;
import com.example.shoppingmall.domain.product.review.dto.req.ProductReviewRequest;
import com.example.shoppingmall.domain.product.review.dto.res.ProductReviewWriteableResponse;
import com.example.shoppingmall.domain.product.review.dto.res.ProductReviewWrittenResponse;
import com.example.shoppingmall.domain.product.review.service.ProductReviewLikeWriteService;
import com.example.shoppingmall.domain.product.review.service.ProductReviewWriteService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductReviewController {
    private final ProductReviewWriteService productReviewWriteService;
    private final ProductReviewLikeWriteService productReviewLikeWriteService;
    private final ReadWriteableReviewUseCase readWriteableReviewUseCase;
    private final ReadWrittenReviewUseCase readWrittenReviewUseCase;

    private final UserReadService userReadService;

    @Operation(summary = "상품 리뷰 생성", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/review/add")
    public ResponseEntity<Void> createProductReview(Principal principal, @RequestBody ProductReviewRequest productReviewRequest){
        User user = userReadService.getUserByEmail(principal.getName());
        productReviewWriteService.createProductReview(user, productReviewRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "작성 가능 리뷰 조회", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/review/writeable/list")
    public List<ProductReviewWriteableResponse> readWriteableReview(Principal principal) {
        User user = userReadService.getUserByEmail(principal.getName());
        return readWriteableReviewUseCase.execute(user);
    }

    @Operation(summary = "작성한 리뷰 조회", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/review/written/list")
    public List<ProductReviewWrittenResponse> readWrittenReview(Principal principal) {
        User user = userReadService.getUserByEmail(principal.getName());
        return readWrittenReviewUseCase.execute(user);
    }

    @Operation(summary = "상품 리뷰 좋아요", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/like/review/{productReviewId}")
    public void createOrDeleteProductReviewLike(Principal principal, @PathVariable Long productReviewId){
        User user = userReadService.getUserByEmail(principal.getName());
        productReviewLikeWriteService.createOrDeleteProductReviewLike(user, productReviewId);
    }
}
