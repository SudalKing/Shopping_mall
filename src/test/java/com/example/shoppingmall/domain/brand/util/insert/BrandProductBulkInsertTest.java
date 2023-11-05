package com.example.shoppingmall.domain.brand.util.insert;

import com.example.shoppingmall.domain.brand.entity.BrandProduct;
import com.example.shoppingmall.domain.brand.util.fixture.BrandProductFixtureFactory;
import com.example.shoppingmall.util.brand.BrandProductBulkInsertCustomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class BrandProductBulkInsertTest {

    @Autowired
    private BrandProductBulkInsertCustomRepository brandProductBulkInsertCustomRepository;

    @DisplayName("[BrandProduct Bulk Insert]")
    @Test
    void bulkInsert(){
        var easyRandom = BrandProductFixtureFactory.get();

        var brandProductList = IntStream.range(0, 1000)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(BrandProduct.class))
                .collect(Collectors.toList());

         brandProductBulkInsertCustomRepository.bulkInsertBrandProduct(brandProductList);
    }
}
