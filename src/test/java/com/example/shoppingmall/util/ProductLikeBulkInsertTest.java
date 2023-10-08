package com.example.shoppingmall.util;

import com.example.shoppingmall.domain.product.entity.ProductLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class ProductLikeBulkInsertTest {

    @Autowired
    private ProductLikeBulkInsertCustomRepository productLikeBulkInsertCustomRepository;

    @DisplayName("[ProductLike Bulk Insert]")
    @Test
    void bulkInsert(){
        var easyRandom = ProductLikeFixtureFactory.get(
                LocalDate.now().minusDays(6),
                LocalDate.now()
        );

        var productsLike = IntStream.range(0, 5000)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(ProductLike.class))
                .collect(Collectors.toList());

        productLikeBulkInsertCustomRepository.bulkInsertProductLike(productsLike);
    }
}
