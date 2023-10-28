package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.ProductReviewDto;
import com.example.shoppingmall.domain.product.entity.ProductReview;
import com.example.shoppingmall.domain.product.repository.ProductReviewLikeRepository;
import com.example.shoppingmall.domain.product.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductReviewReadService {
    private final ProductReviewRepository productReviewRepository;
    private final ProductReviewLikeRepository productReviewLikeRepository;

    public ProductReviewDto getReview(Long productCommentId){
        var comment = productReviewRepository.findProductReviewById(productCommentId);
        return toDto(comment);
    }


    public List<ProductReviewDto> getAllComments(Long productCommentId){
        return productReviewRepository.findAllByProductId(productCommentId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProductReviewDto toDto(ProductReview productReview){
        return new ProductReviewDto(
                productReview.getId(),
                productReview.getProductId(),
                productReview.getUserId(),
                productReview.getContents(),
                productReviewLikeRepository.countAllById(productReview.getId()),
                productReview.getCreatedAt()
        );
    }
}
