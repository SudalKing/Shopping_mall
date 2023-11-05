package com.example.shoppingmall.domain.product.product.service;

import com.example.shoppingmall.domain.brand.entity.BrandProduct;
import com.example.shoppingmall.domain.brand.repository.BrandProductRepository;
import com.example.shoppingmall.domain.brand.dto.req.BrandCategoryRequest;
import com.example.shoppingmall.domain.product.product.dto.req.ProductCommand;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ProductWriteService {
    private final ProductRepository productRepository;
    private final BrandProductRepository brandProductRepository;
    //
    @Transactional
    public Product createProduct(ProductCommand productCommand){
        BrandCategoryRequest brandCategoryRequest = productCommand.getBrandCategoryRequest();

        var product = Product.builder()
                .name(productCommand.getName())
                .price(productCommand.getPrice())
                .stock(productCommand.getStock())
                .description(productCommand.getDescription())
                .saled(productCommand.isSaled())
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        product.validateStockAndPrice();
        Product savedProduct = productRepository.save(product);

        BrandProduct brandProduct = BrandProduct.builder()
                .productId(savedProduct.getId())
                .brandId(brandCategoryRequest.getBrandId())
                .build();
        brandProductRepository.save(brandProduct);

        return savedProduct;
    }

    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);
    }

}
