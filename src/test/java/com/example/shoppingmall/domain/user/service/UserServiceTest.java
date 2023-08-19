package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.RegisterUserCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserWriteService userWriteService;

    @Autowired
    private UserReadService userReadService;


    @Order(1)
    @DisplayName("1. [User 생성 테스트]")
    @Test
    void createUserTest(){
        for (int i = 0; i < 10; i++) {
            var user = registerUserCommand(Integer.toString(i));
            userWriteService.createUser(user);
        }
    }

    @Order(2)
    @DisplayName("2. [User 조회 테스트]")
    @Test
    void readUserTest(){
        userReadService.getAllUsers().forEach(System.out::println);
    }



    public RegisterUserCommand registerUserCommand(String number){
        return RegisterUserCommand.builder()
                .nickname("nickname" + number)
                .phoneNumber("010-1111-1111")
                .email("email" + number + "@test.com")
                .password("password" + number)
                .build();
    }
}