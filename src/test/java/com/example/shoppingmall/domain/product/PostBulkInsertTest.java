package com.example.shoppingmall.domain.product;

import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.util.BulkInsertCustomRepository;
import com.example.shoppingmall.util.ProductFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {

    @Autowired
    private BulkInsertCustomRepository bulkInsertCustomRepository;

    @DisplayName("1. [10만건 Product 삽입]")
    @Test
    void bulkInsert(){
        var easyRandom = ProductFixtureFactory.get(
                LocalDate.of(2010, 1, 1),
                LocalDate.of(2023, 2, 1)
        );

        var objectStopWatch = new StopWatch();
        objectStopWatch.start();

        var products = IntStream.range(0, 100000)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Product.class))
                .collect(Collectors.toList());

        objectStopWatch.stop();
        System.out.println("객체 생성 시간 : " + objectStopWatch.getTotalTimeSeconds());


        var queryStopWatch = new StopWatch();
        queryStopWatch.start();

        bulkInsertCustomRepository.bulkInsertProduct(products);

        queryStopWatch.stop();
        System.out.println("DB Insert 시간 : " + queryStopWatch.getTotalTimeSeconds());
    }
}
