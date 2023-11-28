package com.example.shoppingmall.domain.product.product_like;

import com.example.shoppingmall.domain.product.product_like.ProductLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductLikeReadService {
    private ProductLikeRepository productLikeRepository;

    public int readProductLikeCount(Long productId){
        return productLikeRepository.countAllByProductId(productId);
    }

}
