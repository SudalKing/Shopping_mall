package com.example.shoppingmall.util;

import com.example.shoppingmall.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class ProductBulkInsertTest {

    @Autowired
    private ProductBulkInsertCustomRepository productBulkInsertCustomRepository;

    @DisplayName("[Product Bulk Insert]")
    @Test
    void bulkInsert(){
        var easyRandom = ProductFixtureFactory.get(
                LocalDate.now().minusDays(6),
                LocalDate.now()
        );

        var products = IntStream.range(0, 100)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Product.class))
                .collect(Collectors.toList());

        productBulkInsertCustomRepository.bulkInsertProduct(products);
    }
}
