package com.example.shoppingmall.domain.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomUserDetailServiceTest {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @DisplayName("1. [UserDetailsService Test]")
    @Test
    void test_1(){
        var user = customUserDetailService.loadUserByUsername("1@1");
        System.out.println(user.toString());
    }
}