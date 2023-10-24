package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.brand.entity.ProductBrandCategory;
import com.example.shoppingmall.domain.brand.repository.ProductBrandCategoryRepository;
import com.example.shoppingmall.domain.brand.dto.req.BrandCategoryRequest;
import com.example.shoppingmall.domain.product.dto.req.ProductCommand;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ProductWriteService {
    private final ProductRepository productRepository;
    private final ProductBrandCategoryRepository productBrandCategoryRepository;

    @Transactional
    public Product createProduct(ProductCommand productCommand){
        BrandCategoryRequest brandCategoryRequest = productCommand.getBrandCategoryRequest();

        var product = Product.builder()
                .name(productCommand.getName())
                .price(productCommand.getPrice())
                .stock(productCommand.getStock())
                .description(productCommand.getDescription())
                .typeId(productCommand.getTypeId())
                .saled(productCommand.isSaled())
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        product.validateStockAndPrice();
        Product savedProduct = productRepository.save(product);

        ProductBrandCategory productBrandCategory = ProductBrandCategory.builder()
                .productId(savedProduct.getId())
                .brandId(brandCategoryRequest.getBrandId())
                .brandCategoryId(brandCategoryRequest.getCategoryId())
                .build();
        productBrandCategoryRepository.save(productBrandCategory);

        return savedProduct;
    }

    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);
    }

}
