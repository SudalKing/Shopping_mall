package com.example.shoppingmall.domain.product.product_like.service;

import com.example.shoppingmall.domain.product.product_like.entity.ProductLike;
import com.example.shoppingmall.domain.product.product_like.repository.ProductLikeRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductLikeWriteService {
    private final ProductLikeRepository productLikeRepository;

    public void createOrDeleteProductLike(final User user, final Long productId){
        Optional<ProductLike> findProductLike = productLikeRepository
                .findByUserIdAndProductId(user.getId(), productId);

        if (findProductLike.isEmpty()) {
            ProductLike productLike = ProductLike.builder()
                    .userId(user.getId())
                    .productId(productId)
                    .build();
            productLikeRepository.save(productLike);
        } else {
            productLikeRepository.delete(findProductLike.get());
        }
    }

    public void deleteProductLikeByUserId(final Long userId) {
        productLikeRepository.deleteAllByUserId(userId);
    }
}
