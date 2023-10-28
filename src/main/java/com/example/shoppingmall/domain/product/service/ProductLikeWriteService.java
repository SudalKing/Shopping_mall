package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.entity.ProductLike;
import com.example.shoppingmall.domain.product.repository.ProductLikeRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductLikeWriteService {
    private final ProductLikeRepository productLikeRepository;

    public void createOrDeleteProductLike(User user, Long productId){
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

}
