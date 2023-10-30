package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.product.review.CreateReviewUseCase;
import com.example.shoppingmall.application.usecase.product.review.ReadReviewDetailUseCase;
import com.example.shoppingmall.application.usecase.product.review.ReadWriteableReviewUseCase;
import com.example.shoppingmall.application.usecase.product.review.ReadWrittenReviewUseCase;
import com.example.shoppingmall.domain.awsS3.service.ProductReviewImageWriteService;
import com.example.shoppingmall.domain.product.review.dto.req.ProductReviewRequest;
import com.example.shoppingmall.domain.product.review.dto.res.ProductReviewDetailResponse;
import com.example.shoppingmall.domain.product.review.dto.res.ProductReviewWriteableResponse;
import com.example.shoppingmall.domain.product.review.dto.res.ProductReviewWrittenResponse;
import com.example.shoppingmall.domain.product.review.service.ProductReviewLikeWriteService;
import com.example.shoppingmall.domain.product.review.service.ProductReviewWriteService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductReviewController {
    private final ProductReviewWriteService productReviewWriteService;
    private final ProductReviewLikeWriteService productReviewLikeWriteService;
    private final ProductReviewImageWriteService productReviewImageWriteService;
    private final ReadWriteableReviewUseCase readWriteableReviewUseCase;
    private final CreateReviewUseCase createReviewUseCase;
    private final ReadWrittenReviewUseCase readWrittenReviewUseCase;
    private final ReadReviewDetailUseCase readReviewDetailUseCase;

    private final UserReadService userReadService;

    @Operation(summary = "상품 리뷰 생성", description = "[인증 필요]")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/review/add")
    public ResponseEntity<Long> createProductReview(Principal principal,
                                                    @RequestPart ProductReviewRequest productReviewRequest,
                                                    @RequestPart(value = "image", required = false) List<MultipartFile> multipartFiles){
        User user = userReadService.getUserByEmail(principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createReviewUseCase.execute(user, productReviewRequest, multipartFiles));
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

    @Operation(summary = "리뷰 상세 조회", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/review/detail/list")
    public ResponseEntity<ProductReviewDetailResponse> readReviewInfo(Principal principal, Long reviewId) {
        User user = userReadService.getUserByEmail(principal.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(readReviewDetailUseCase.execute(user, reviewId));
    }

    @Operation(summary = "리뷰 수정", description = "[인증 필요]")
    @ApiResponse(responseCode = "204", description = "OK")
    @PutMapping("/review/update/{reviewId}")
    public ResponseEntity<Void> updateReview(Principal principal, @PathVariable Long reviewId, @RequestBody Map<String, Object> updates){
        User user = userReadService.getUserByEmail(principal.getName());
        productReviewWriteService.updateReview(reviewId, updates);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "리뷰 이미지 수정", description = "[인증 필요]")
    @ApiResponse(responseCode = "204", description = "OK")
    @PutMapping("/review/update/{reviewId}/image")
    public ResponseEntity<Void> updateReviewImage(Principal principal,
                                                  @PathVariable Long reviewId,
                                                  @RequestPart(value = "image") List<MultipartFile> multipartFiles){
        User user = userReadService.getUserByEmail(principal.getName());
        productReviewImageWriteService.updateProductReviewImage(reviewId, multipartFiles);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 리뷰 좋아요", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/like/review/{productReviewId}")
    public void createOrDeleteProductReviewLike(Principal principal, @PathVariable Long productReviewId){
        User user = userReadService.getUserByEmail(principal.getName());
        productReviewLikeWriteService.createOrDeleteProductReviewLike(user, productReviewId);
    }
}
