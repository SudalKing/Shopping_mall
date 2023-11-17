package com.example.shoppingmall.domain.product.product.service.cursor;

import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductBestReadService {
    private final ProductRepository productRepository;

    private final ProductReadService productReadService;

    public List<ProductResponse> getAllBestProducts() {
        List<Product> productList = findAllBestProducts();

        return productList.stream()
                .map(productReadService::toProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getBestProducts(Long categoryId, Long subCategoryId) {
        List<Product> productList = findBestProducts(categoryId, subCategoryId);

        return productList.stream()
                .map(productReadService::toProductResponse)
                .collect(Collectors.toList());
    }
    //     categoryId == 1 의류, subCategoryId 는 의류에만 사용
    private List<Product> findBestProducts(Long categoryId, Long subCategoryId) {
        if (categoryId == 1L) {
            return productRepository.findTop3ByCategoryAndSubCategoryIdOrderByStockDesc(categoryId, subCategoryId);
        } else {
            return productRepository.findTop3ByCategoryIdOrderByStockDesc(categoryId);
        }
    }

    private List<Product> findAllBestProducts() {
        List<Product> allBestProducts = new ArrayList<>();

        List<Product> clothes1 = findBestProducts(1L, 1L);
        List<Product> clothes2 = findBestProducts(1L, 2L);
        List<Product> clothes3 = findBestProducts(1L, 3L);
        List<Product> clothes4 = findBestProducts(1L, 4L);
        List<Product> clothes5 = findBestProducts(1L, 5L);

        List<Product> props = findBestProducts(2L, 0L);
        List<Product> goods = findBestProducts(3L, 0L);
        List<Product> homeLivings = findBestProducts(4L, 0L);
        List<Product> beauty = findBestProducts(5L, 0L);

        allBestProducts.addAll(clothes1);
        allBestProducts.addAll(clothes2);
        allBestProducts.addAll(clothes3);
        allBestProducts.addAll(clothes4);
        allBestProducts.addAll(clothes5);

        allBestProducts.addAll(props);
        allBestProducts.addAll(goods);
        allBestProducts.addAll(homeLivings);
        allBestProducts.addAll(beauty);

        return allBestProducts;
    }
}
