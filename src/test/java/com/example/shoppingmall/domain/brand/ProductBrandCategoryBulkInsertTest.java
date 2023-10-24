package com.example.shoppingmall.domain.brand;

import com.example.shoppingmall.domain.brand.entity.ProductBrandCategory;
import com.example.shoppingmall.util.brand.ProductBrandCategoryBulkInsertCustomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class ProductBrandCategoryBulkInsertTest {

    @Autowired
    private ProductBrandCategoryBulkInsertCustomRepository productBrandCategoryBulkInsertCustomRepository;

    @DisplayName("[BrandCategory Bulk Insert]")
    @Test
    void bulkInsert(){
        var easyRandom = ProductBrandCategoryFixtureFactory.get();

        var productBrandCategoryList = IntStream.range(0, 500)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(ProductBrandCategory.class))
                .collect(Collectors.toList());

        productBrandCategoryBulkInsertCustomRepository.bulkInsertProductBrandCategory(productBrandCategoryList);
    }
}
