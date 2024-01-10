package com.example.shoppingmall.domain.awsS3.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductReviewImageRepositoryTest {

    @Autowired
    private ProductReviewImageRepository productReviewImageRepository;

    @Test
    void findUploadFileUrlsByReviewId() {
        List<String> urls = productReviewImageRepository.findUploadFileUrlsByReviewId(23L);

//        urls.forEach(System.out::println);
        System.out.println(urls.get(0));
    }
}