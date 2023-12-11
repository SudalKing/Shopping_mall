package com.example.shoppingmall.domain.product.product.service;

import com.example.shoppingmall.domain.awsS3.service.ProductImageReadService;
import com.example.shoppingmall.domain.brand.util.CategoryIdsMapping;
import com.example.shoppingmall.domain.product.clothes.service.ClothesReadService;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.sale.service.SaleReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProductUtilService {
    private final ProductRepository productRepository;

    private final ProductImageReadService productImageReadService;
    private final ClothesReadService clothesReadService;
    private final SaleReadService saleReadService;

    public List<CategoryIdsMapping> getCategoryIdsMapping(final Long brandId) {
        return productRepository.findCategoryIdsByBrandId(brandId);
    }

    public String getProductImageUrl(final Long productId) {
        return productImageReadService.getUrl(productId);
    }

    public Map<String, String> getClothesInfo(final Product product) {
        return clothesReadService.getClothesInfo(product);
    }

    public int getDiscountPrice(final Product product) {
        return saleReadService.getDiscountPrice(product);
    }
}
