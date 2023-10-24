package com.example.shoppingmall.domain.product.repository;

import com.example.shoppingmall.domain.product.dto.res.BrandProductResponse;
import com.example.shoppingmall.domain.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findTopByOrderByPrice() {
        var product = productRepository.findTopByOrderByPriceDesc();
        System.out.println(product.getPrice());
    }

    @Test
    void a(){
        List<Product> products = productRepository.findAllProductsByCursorNoKeyOrderByScore(10);
        products.stream().forEach(
                product -> {
                    System.out.println(product.getId());
                }
        );
    }

    @Test
    void findProductsByBrandIdNoKeyOrderByScore() {
    }
//
//    @Test
//    void findProductsByBrandIdHasKeyOrderByPriceAsc() {
//        List<BrandProductResponse> brandProductResponseList = productRepository.findProductsByBrandIdHasKeyOrderByPriceAsc(400L, 0L, 1L, 10);
//        brandProductResponseList.forEach(
//                brandProductResponse -> {
//                    System.out.println("response Id: " + brandProductResponse.getId());
//                    System.out.println("response BrandCategoryId: " + brandProductResponse.getBrandCategoryId());
//                }
//        );
//    }
}