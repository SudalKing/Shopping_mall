package com.example.shoppingmall.domain.product.util;

import com.example.shoppingmall.domain.brand.ProductBrandCategoryFixtureFactory;
import com.example.shoppingmall.domain.brand.entity.ProductBrandCategory;
import com.example.shoppingmall.domain.product_util.entity.Best;
import com.example.shoppingmall.util.brand.ProductBrandCategoryBulkInsertCustomRepository;
import com.example.shoppingmall.util.product.BestBulkInsertCustomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class BestBulkInsertTest {

    @Autowired
    private BestBulkInsertCustomRepository bestBulkInsertCustomRepository;

    @DisplayName("[Best Bulk Insert]")
    @Test
    void bulkInsert(){
        var easyRandom = BestFixtureFactory.get();

        var bestList = IntStream.range(0, 500)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Best.class))
                .collect(Collectors.toList());

        bestBulkInsertCustomRepository.bulkInsertBest(bestList);
    }
}
