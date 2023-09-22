package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.entity.ProductLike;
import com.example.shoppingmall.domain.product.repository.ProductLikeRepository;
import com.example.shoppingmall.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ProductLikeWriteService {
    private final ProductLikeRepository productLikeRepository;

    public Long createProductLike(ProductDto productDto, UserDto userDto){
        var productLike = ProductLike.builder()
                .productId(productDto.getId())
                .userId(userDto.getId())
                .createdAt(LocalDateTime.now())
                .build();
        return productLikeRepository.save(productLike).getId();
    }

    public void cancelProductLike(){

    }

}
