package com.example.shoppingmall.domain.product_util.service;

import com.example.shoppingmall.domain.product_util.entity.Best;
import com.example.shoppingmall.domain.product_util.repository.BestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BestReadService {
    private final BestRepository bestRepository;

    public Best readBestByProductId(Long productId){
        return bestRepository.findBestByProductId(productId);
    }
}
