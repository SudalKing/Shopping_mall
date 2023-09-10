package com.example.shoppingmall.domain.awsS3.service;

import com.example.shoppingmall.domain.awsS3.dto.S3FileDto;
import com.example.shoppingmall.domain.awsS3.entity.ProductImage;
import com.example.shoppingmall.domain.awsS3.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductImageWriteService {
    private final ProductImageRepository productImageRepository;

    @Transactional
    public void createProductImage(Long productId, List<S3FileDto> s3FileDtoList){
        if (s3FileDtoList == null) {
            var productImage = ProductImage.builder()
                    .productId(productId)
                    .originalFileName(null)
                    .uploadFileName(null)
                    .uploadFilePath(null)
                    .uploadFileUrl(null)
                    .deleted(false)
                    .build();
            productImageRepository.save(productImage);
        } else {
            for (var s3fileDto : s3FileDtoList) {
                var productImage = ProductImage.builder()
                        .productId(productId)
                        .originalFileName(s3fileDto.getOriginalFileName())
                        .uploadFileName(s3fileDto.getUploadFileName())
                        .uploadFilePath(s3fileDto.getUploadFilePath())
                        .uploadFileUrl(s3fileDto.getUploadFileUrl())
                        .deleted(false)
                        .build();
                productImageRepository.save(productImage);
            }
        }
    }

    public void deleteProductImage(Long productId){
        productImageRepository.deleteAllByProductId(productId);
    }
}
