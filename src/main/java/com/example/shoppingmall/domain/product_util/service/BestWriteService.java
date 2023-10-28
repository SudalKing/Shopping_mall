package com.example.shoppingmall.domain.product_util.service;

import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.repository.ProductLikeRepository;
import com.example.shoppingmall.domain.product_util.entity.Best;
import com.example.shoppingmall.domain.product_util.repository.BestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BestWriteService {

    private final BestRepository bestRepository;
    private final ProductLikeRepository productLikeRepository;
    private final ProductReadService productReadService;

    public Best createBest(ProductDto productDto){
        var best = Best.builder()
                .productId(productDto.getId())
                .totalLike(0)
                .totalSales(0)
                .score(0.)
                .build();
        return bestRepository.save(best);
    }

    public void updateScore(Long productId){
        var productBest = bestRepository.findBestByProductId(productId);
        Double score = calcScore(productId);
        productBest.updateScore(score);
    }

    public void updateTotalSales(Long productId, int stock){
        var productBest = bestRepository.findBestByProductId(productId);
        productBest.updateTotalSales(stock);
    }

    public void updateTotalLike(Long productId){
        var productBest = bestRepository.findBestByProductId(productId);
        int likeCount = productLikeRepository.findLikeCountByProductId(productId);
        productBest.updateTotalLike(likeCount);
    }

    public Double calcScore(Long productId){
        var productBest = bestRepository.findBestByProductId(productId);
        var productDto = productReadService.getProduct(productId);

        int highestPrice = productReadService.readHighestPrice();
        int highestSales = bestRepository.findTopByOrderByTotalSalesDesc().getTotalSales();
        int highestLike = productLikeRepository.findLikeCountMostLike();

        return  (1 - productDto.getPrice() / highestPrice) * 0.2
                + (productBest.getTotalSales() / highestSales) * 0.4
                + (productBest.getTotalLike() / highestLike) * 0.4;

    }
}
