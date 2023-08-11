package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.product.repository.ProductLikeRepository;
import com.example.shoppingmall.domain.product.repository.ProductRepository;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
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

    public PageCursor<Product> getProductsByCursor(CursorRequest cursorRequest){
//        var products = productRepository.findProductsByCursor(cursorRequest.getKey(), cursorRequest.getSize());
//        var nextKey = getNextKey(products);
//
//        return new PageCursor<>(cursorRequest.next(nextKey), products);
        var products = findAllBy(cursorRequest);
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), products);
    }

    private List<Product> findAllBy(CursorRequest cursorRequest) {
        if(cursorRequest.hasKey()){
            return productRepository.findAllProductsByCursorHasKey(cursorRequest.getKey(), cursorRequest.getSize());
        } else{
            return productRepository.findAllProductsByCursorNoKey(cursorRequest.getSize());
        }
    }

    public ProductDto toDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getModelName(),
                product.getPrice(),
                product.getStock(),
                product.getDescription(),
                product.getCategoryId(),
                product.isDeleted(),
                productLikeRepository.countAllByProductId(product.getId())
        );
    }

    private Long getNextKey(List<Product> products){
        return products.stream()
                .mapToLong(Product::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }

}
