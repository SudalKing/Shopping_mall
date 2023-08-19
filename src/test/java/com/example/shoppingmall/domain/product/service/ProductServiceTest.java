package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.product.dto.ProductCommand;
import com.example.shoppingmall.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductWriteService productWriteService;

    @Autowired
    private ProductReadService productReadService;

    @DisplayName("1. [Product 저장 테스트]")
    @Test
    void test_1(){
        Product product = productWriteService.createProduct(command());
        System.out.println("Version: " + product.getVersion());
    }

    @DisplayName("2. [낙관적 락 테스트]")
    @Test
    void test_2(){
        Product product = productWriteService.updateProduct(1L, command());
        Assert.isTrue(product.getVersion() == 1, "Version이 증가하지 않음");
    }

    public ProductCommand command(){
        return ProductCommand.builder()
                .name("test1")
                .modelName("test1")
                .description("testtest")
                .price(1000)
                .stock(1)
                .categoryId(1L)
                .build();
    }
}