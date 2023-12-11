package com.example.shoppingmall.domain.brand.service;

import com.example.shoppingmall.domain.brand.repository.BrandLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BrandLikeReadService {
    private final BrandLikeRepository brandLikeRepository;

    public boolean isBrandLiked(final Long userId, final Long brandId) {
        return brandLikeRepository.findByUserIdAndBrandId(userId, brandId).isPresent();
    }
}
