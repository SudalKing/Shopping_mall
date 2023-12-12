package com.example.shoppingmall.application.usecase.product;

import com.example.shoppingmall.domain.awsS3.service.ProductImageWriteService;
import com.example.shoppingmall.domain.product.best.service.BestWriteService;
import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.dto.req.ProductCommand;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.product.service.ProductWriteService;
import com.example.shoppingmall.domain.awsS3.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreateProductUseCase {
    private final ProductWriteService productWriteService;
    private final ProductReadService productReadService;
    private final ProductImageWriteService productImageWriteService;
    private final BestWriteService bestWriteService;
    private final AmazonS3Service amazonS3Service;

    public ProductResponse execute(ProductCommand productCommand, String fileType, List<MultipartFile> multipartFiles){
        var product = productWriteService.createProduct(productCommand);
        var s3FileDtoList = amazonS3Service.uploadFiles(fileType, multipartFiles);
        var best = bestWriteService.createBest(productReadService.toProductResponse(product));

        productImageWriteService.createProductImage(product.getId(), s3FileDtoList);

        return productReadService.getProductResponse(product.getId());
    }
}
