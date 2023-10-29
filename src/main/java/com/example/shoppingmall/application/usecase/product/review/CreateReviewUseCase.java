package com.example.shoppingmall.application.usecase.product.review;

import com.example.shoppingmall.domain.awsS3.dto.S3FileDto;
import com.example.shoppingmall.domain.awsS3.service.ProductReviewImageWriteService;
import com.example.shoppingmall.domain.product.review.dto.req.ProductReviewRequest;
import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import com.example.shoppingmall.domain.product.review.service.ProductReviewWriteService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.util.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreateReviewUseCase {
    private final ProductReviewWriteService productReviewWriteService;
    private final ProductReviewImageWriteService productReviewImageWriteService;
    private final AmazonS3Service amazonS3Service;

    @Transactional
    public Long execute(User user, ProductReviewRequest productReviewRequest, List<MultipartFile> multipartFiles) {
        List<S3FileDto> s3FileDtoList = amazonS3Service.uploadFiles("image", multipartFiles);
        ProductReview productReview = productReviewWriteService.createProductReview(user, productReviewRequest);

        productReviewImageWriteService.createProductReviewImage(productReview.getId(), s3FileDtoList);

        return productReview.getId();
    }
}
