package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.ProductCommentDto;
import com.example.shoppingmall.domain.product.entity.ProductComment;
import com.example.shoppingmall.domain.product.repository.ProductCommentLikeRepository;
import com.example.shoppingmall.domain.product.repository.ProductCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductCommentReadService {
    private final ProductCommentRepository productCommentRepository;
    private final ProductCommentLikeRepository productCommentLikeRepository;

    public ProductCommentDto getComment(Long productCommentId){
        var comment = productCommentRepository.findProductCommentById(productCommentId);
        return toDto(comment);
    }

    public List<ProductCommentDto> getAllComments(Long productCommentId){
        return productCommentRepository.findAllByProductId(productCommentId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProductCommentDto toDto(ProductComment productComment){
        return new ProductCommentDto(
                productComment.getId(),
                productComment.getProductId(),
                productComment.getUserId(),
                productComment.getContents(),
                productCommentLikeRepository.countAllByCommentId(productComment.getId()),
                productComment.getCreatedAt()
        );
    }
}
