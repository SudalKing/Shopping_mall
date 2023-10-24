package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.req.ProductCommentCommand;
import com.example.shoppingmall.domain.product.entity.ProductComment;
import com.example.shoppingmall.domain.product.repository.ProductCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductCommentWriteService {
    private final ProductCommentRepository productCommentRepository;

    public ProductComment createProductComment(Long userId, ProductCommentCommand productCommentCommand){
        var productComment = ProductComment.builder()
                .productId(productCommentCommand.getProductId())
                .userId(userId)
                .contents(productCommentCommand.getContents())
                .build();
        return productCommentRepository.save(productComment);
    }

    public void deleteProductComment(Long commentId, Long userId){
        var comment = productCommentRepository.findById(commentId).orElseThrow();
        if(comment.getUserId().equals(userId)) productCommentRepository.deleteById(commentId);
        else throw new RuntimeException();
    }
}
