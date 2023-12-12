package com.example.shoppingmall.domain.awsS3.service;

import com.example.shoppingmall.domain.awsS3.entity.ProductReviewImage;
import com.example.shoppingmall.domain.awsS3.repository.ProductReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductReviewImageReadService {
    private final ProductReviewImageRepository productReviewImageRepository;

    public List<ProductReviewImage> readImages(Long reviewId){
        return productReviewImageRepository.findAllByReviewId(reviewId);
    }

    public String getUrl(Long reviewId) {
        List<String> productImagesUrlList = productReviewImageRepository
                .findUploadFileUrlsByReviewId(reviewId);

        if (productImagesUrlList.isEmpty()) {
            return "";
        } else {
            return productImagesUrlList.get(0);
        }
    }

    public Integer getPhotoReviewCount(Long productId) {
        var count = productReviewImageRepository.countPhotoAllByProductId(productId);

        if (count == null) {
            return 0;
        }

        return count;
    }

//
//    public List<String> getUrls(Long reviewId){
//        List<String> productImagesUrlList = productReviewImageRepository
//                .findUploadFileUrlsByReviewId(reviewId);
//
//        return productImagesUrlList;
//    }
}
