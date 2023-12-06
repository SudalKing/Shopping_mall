package com.example.shoppingmall.domain.product.clothes.service;

import com.example.shoppingmall.domain.product.clothes.entity.ClothesProduct;
import com.example.shoppingmall.domain.product.clothes.repository.ClothesProductRepository;
import com.example.shoppingmall.domain.product.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ClothesReadService {
    private final ClothesProductRepository clothesProductRepository;

    public Map<String, String> getClothesInfo(Product product) {
        Long typeId = product.getCategoryId();
        Map<String, String> clothesInfo = new HashMap<>();
        ClothesProduct clothesProduct = clothesProductRepository.findByProductId(product.getId());

        if (typeId == 1L) {
            clothesInfo.put("color", clothesProduct.getColor());
            clothesInfo.put("size", clothesProduct.getClotheSize());
        } else {
            clothesInfo.put("color", "");
            clothesInfo.put("size", "");
        }

        return clothesInfo;
    }
}
