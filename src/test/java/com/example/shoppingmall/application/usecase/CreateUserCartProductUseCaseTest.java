package com.example.shoppingmall.application.usecase;

import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateUserCartProductUseCaseTest {
    @Autowired
    private CreateUserCartProductUseCase createUserCartProductUseCase;

    @Autowired
    private UserRepository userRepository;

    private Long userId = 5L;
    private Long userId2 = 6L;

    @DisplayName("1. [장바구니 생성 테스트]")
    @Test
    void test_1(){
        createUserCartProductUseCase.execute(userId, 5L, 5);
        createUserCartProductUseCase.execute(userId, 1L, 5);
//        createUserCartProductUseCase.execute(userId, 3L, 5);
//        createUserCartProductUseCase.execute(userId2, 1L, 4);
//        createUserCartProductUseCase.execute(userId2, 1L, 6);
    }

    @DisplayName("2. [장바구니 조회 테스트]")
    @Test
    void test_2(){
        var user = userRepository.findById(userId).orElseThrow();
        var user2 = userRepository.findUserById(userId2);

        System.out.println(user.getNickname() + "의 장바구니 번호: " + user.getCart().getId());
        for (int i = 0; i < user.getCart().getCartProducts().size(); i++) {
            System.out.println("장바구니 물품 번호: " + user.getCart().getCartProducts().get(i).getId());
            System.out.println("물품 번호: " + user.getCart().getCartProducts().get(i).getProduct().getId());
        }


        System.out.println(user2.getNickname() + "의 장바구니 번호: " + user2.getCart().getId());
        for (int i = 0; i < user2.getCart().getCartProducts().size(); i++) {
            System.out.println("장바구니 물품 번호: " + user2.getCart().getCartProducts().get(i).getId());
            System.out.println("물품 번호: " + user2.getCart().getCartProducts().get(i).getProduct().getId());
        }

    }

}