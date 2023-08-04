package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.entity.Category;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.product.repository.ProductLikeRepository;
import com.example.shoppingmall.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductReadService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;

    public ProductDto getProduct(Long productId) {
        var product = productRepository.findProductById(productId);
        return toDto(product);
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getAllProductsByCategory(Category category){
        return productRepository.findAllByCategory(category)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto toDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getModelName(),
                product.getPrice(),
                product.getStock(),
                product.getDescription(),
                product.getCategory(),
                product.isDeleted(),
                productLikeRepository.countAllByProductId(product.getId())
        );
    }

}
