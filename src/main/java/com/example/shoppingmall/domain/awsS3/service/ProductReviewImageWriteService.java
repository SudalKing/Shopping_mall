package com.example.shoppingmall.domain.awsS3.service;

import com.example.shoppingmall.domain.awsS3.dto.S3FileDto;
import com.example.shoppingmall.domain.awsS3.entity.ProductReviewImage;
import com.example.shoppingmall.domain.awsS3.repository.ProductReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductReviewImageWriteService {
    private final ProductReviewImageRepository productReviewImageRepository;
    private final AmazonS3Service amazonS3Service;

    @Transactional
    public void createProductReviewImage(Long reviewId, List<S3FileDto> s3FileDtoList){
        if (s3FileDtoList.isEmpty()) {
            ProductReviewImage productReviewImage = ProductReviewImage.builder()
                    .reviewId(reviewId)
                    .originalFileName(null)
                    .uploadFileName(null)
                    .uploadFilePath(null)
                    .uploadFileUrl(null)
                    .build();
            productReviewImageRepository.save(productReviewImage);
        } else {
            for (var s3fileDto : s3FileDtoList) {
                ProductReviewImage productReviewImage = ProductReviewImage.builder()
                        .reviewId(reviewId)
                        .originalFileName(s3fileDto.getOriginalFileName())
                        .uploadFileName(s3fileDto.getUploadFileName())
                        .uploadFilePath(s3fileDto.getUploadFilePath())
                        .uploadFileUrl(s3fileDto.getUploadFileUrl())
                        .build();
                productReviewImageRepository.save(productReviewImage);
            }
        }
    }

    @Transactional
    public void updateProductReviewImage(Long reviewId, Optional<List<MultipartFile>> multipartFiles) {
        if (multipartFiles.isEmpty()) {
            deleteProductReviewImage(reviewId);
        } else {
            List<S3FileDto> s3FileDtoList = amazonS3Service.uploadFiles("image", multipartFiles.get());
            deleteProductReviewImage(reviewId);
            createProductReviewImage(reviewId, s3FileDtoList);
        }
    }

    public void deleteProductReviewImage(Long reviewId){
        productReviewImageRepository.deleteByReviewId(reviewId);
    }
}
