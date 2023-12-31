package com.example.shoppingmall.domain.awsS3.service;

import com.example.shoppingmall.domain.awsS3.entity.ProductImage;
import com.example.shoppingmall.domain.awsS3.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductImageReadService {
    private final ProductImageRepository productImageRepository;

    public List<ProductImage> readImages(Long productId){
        return productImageRepository.findAllByProductId(productId);
    }

    public String getUrl(Long productId) {
        List<String> productsUrlList = productImageRepository
                .findUploadFileUrlsByProductId(productId);

        if (productsUrlList.isEmpty()) {
            return "";
        } else {
            return productsUrlList.get(0);
        }
    }
}
