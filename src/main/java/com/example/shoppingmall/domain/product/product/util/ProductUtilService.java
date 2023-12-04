package com.example.shoppingmall.domain.product.product.util;

import com.example.shoppingmall.domain.brand.service.BrandReadService;
import com.example.shoppingmall.domain.brand.util.BrandInfoMapping;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.sale.service.SaleReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductUtilService {
    private final SaleReadService saleReadService;
    private final BrandReadService brandReadService;

    public int getDiscountPrice(Product product) {
        return saleReadService.getDiscountPrice(product);
    }

    public BrandInfoMapping getBrandInfo(Long productId) {
        return brandReadService.getBrandInfo(productId);
    }
}
