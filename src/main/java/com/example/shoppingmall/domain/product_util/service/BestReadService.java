package com.example.shoppingmall.domain.product_util.service;

import com.example.shoppingmall.domain.product_util.entity.BestProduct;
import com.example.shoppingmall.domain.product_util.repository.BestProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BestReadService {
    private final BestProductRepository bestProductRepository;

    public BestProduct readBestByProductId(Long productId){
        return bestProductRepository.findBestByProductId(productId);
    }
}
