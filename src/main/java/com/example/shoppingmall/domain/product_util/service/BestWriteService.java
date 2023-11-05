package com.example.shoppingmall.domain.product_util.service;

import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.repository.ProductLikeRepository;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.product_util.entity.BestProduct;
import com.example.shoppingmall.domain.product_util.repository.BestProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BestWriteService {

    private final BestProductRepository bestProductRepository;
    private final ProductLikeRepository productLikeRepository;
    private final ProductReadService productReadService;

    public BestProduct createBest(ProductResponse productResponse){
        var best = BestProduct.builder()
                .productId(productResponse.getId())
                .totalLike(0)
                .totalSales(0)
                .score(0.)
                .build();
        return bestProductRepository.save(best);
    }

    public void updateScore(Long productId){
        var productBest = bestProductRepository.findBestByProductId(productId);
        Double score = calcScore(productId);
        productBest.updateScore(score);
    }

    public void updateTotalSales(Long productId, int stock){
        var productBest = bestProductRepository.findBestByProductId(productId);
        productBest.updateTotalSales(stock);
    }

    public void updateTotalLike(Long productId){
        var productBest = bestProductRepository.findBestByProductId(productId);
        int likeCount = productLikeRepository.findLikeCountByProductId(productId);
        productBest.updateTotalLike(likeCount);
    }

    public Double calcScore(Long productId){
        var productBest = bestProductRepository.findBestByProductId(productId);
        var productDto = productReadService.getProduct(productId);

        int highestPrice = productReadService.readHighestPrice();
        int highestSales = bestProductRepository.findTopByOrderByTotalSalesDesc().getTotalSales();
        int highestLike = productLikeRepository.findLikeCountMostLike();

        return  (1 - productDto.getPrice() / highestPrice) * 0.2
                + (productBest.getTotalSales() / highestSales) * 0.4
                + (productBest.getTotalLike() / highestLike) * 0.4;

    }
}
