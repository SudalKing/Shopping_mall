package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.ProductCommentDto;
import com.example.shoppingmall.domain.product.entity.ProductCommentLike;
import com.example.shoppingmall.domain.product.repository.ProductCommentLikeRepository;
import com.example.shoppingmall.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductCommentLikeWriteService {
    private final ProductCommentLikeRepository productCommentLikeRepository;

    public Long createProductCommentLike(ProductCommentDto productCommentDto, UserDto userDto){
        var productCommentLike = ProductCommentLike.builder()
                .commentId(productCommentDto.getId())
                .userId(userDto.getId())
                .build();
        return productCommentLikeRepository.save(productCommentLike).getId();
    }

}
