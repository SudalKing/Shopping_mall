package com.example.shoppingmall.domain.product.product_like.service;

import com.example.shoppingmall.domain.product.product_like.repository.ProductLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductLikeReadService {
    private final ProductLikeRepository productLikeRepository;

    public int getProductLikeCount(Long productId){
        return productLikeRepository.countAllByProductId(productId);
    }

    public boolean isLiked(Long userId, Long productId) {
        return productLikeRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }
}
