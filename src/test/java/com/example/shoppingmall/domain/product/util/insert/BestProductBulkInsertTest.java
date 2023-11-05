package com.example.shoppingmall.domain.product.util.insert;

import com.example.shoppingmall.domain.product.util.fixture.BestProductFixtureFactory;
import com.example.shoppingmall.domain.product_util.entity.BestProduct;
import com.example.shoppingmall.util.product.BestProductBulkInsertCustomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class BestProductBulkInsertTest {

    @Autowired
    private BestProductBulkInsertCustomRepository bestProductBulkInsertCustomRepository;

    @DisplayName("[BestProduct Bulk Insert]")
    @Test
    void bulkInsert(){
        var easyRandom = BestProductFixtureFactory.get();

        var bestList = IntStream.range(0, 1000)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(BestProduct.class))
                .collect(Collectors.toList());

        bestProductBulkInsertCustomRepository.bulkInsertBest(bestList);
    }
}
