package com.example.shoppingmall.application.usecase.product;

import com.example.shoppingmall.domain.awsS3.service.ProductImageReadService;
import com.example.shoppingmall.domain.awsS3.service.ProductImageWriteService;
import com.example.shoppingmall.domain.product.service.ProductWriteService;
import com.example.shoppingmall.util.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class DeleteProductUseCase {
    private final ProductWriteService productWriteService;
    private final ProductImageWriteService productImageWriteService;
    private final ProductImageReadService productImageReadService;
    private final AmazonS3Service amazonS3Service;

    public void execute(Long productId){
        var productImages = productImageReadService.readImages(productId);

        for (var productImage: productImages) {
            amazonS3Service.deleteFile(productImage.getUploadFilePath(), productImage.getUploadFileName());
        }
        productImageWriteService.deleteProductImage(productId);
        productWriteService.deleteProduct(productId);
    }
}
