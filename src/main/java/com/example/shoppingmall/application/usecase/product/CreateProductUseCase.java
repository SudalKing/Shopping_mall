package com.example.shoppingmall.application.usecase.product;

import com.example.shoppingmall.domain.awsS3.service.ProductImageWriteService;
import com.example.shoppingmall.domain.product.dto.ProductCommand;
import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.service.ProductWriteService;
import com.example.shoppingmall.util.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CreateProductUseCase {
    private final ProductWriteService productWriteService;
    private final ProductReadService productReadService;
    private final ProductImageWriteService productImageWriteService;
    private final AmazonS3Service amazonS3Service;

    public ProductDto execute(ProductCommand productCommand, String fileType, List<MultipartFile> multipartFiles){
        var product = productWriteService.createProduct(productCommand);
        var s3FileDtoList = amazonS3Service.uploadFiles(fileType, multipartFiles);

        productImageWriteService.createProductImage(product.getId(), s3FileDtoList);

        return productReadService.getProduct(product.getId());
    }
}