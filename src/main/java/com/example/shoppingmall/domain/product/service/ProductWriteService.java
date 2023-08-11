package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.ProductCommand;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductWriteService {
    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(ProductCommand productCommand){
        var product = Product.builder()
                .name(productCommand.getName())
                .modelName(productCommand.getModelName())
                .price(productCommand.getPrice())
                .stock(productCommand.getStock())
                .description(productCommand.getDescription())
                .categoryId(productCommand.getCategoryId())
                .build();
        product.validateStockAndPrice();

        return productRepository.save(product);
    }

    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);
    }

    @Transactional
    public Product updateProduct(Long productId, ProductCommand productCommand){
        var product = productRepository.findProductById(productId);
        product.update(
                productCommand.getName(),
                productCommand.getModelName(),
                productCommand.getPrice(),
                productCommand.getDescription()
        );
        return product;
    }

}
