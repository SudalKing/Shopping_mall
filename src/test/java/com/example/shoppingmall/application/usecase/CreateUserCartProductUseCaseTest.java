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
    private CartProductRepository cartProductRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("[장바구니 테스트]")
    @Test
    void test_1(){
//        createUserCartProductUseCase.execute(1L, 1L, 3);
        createUserCartProductUseCase.execute(1L, 5L, 6);
    }

    @DisplayName("2. [장바구니 조회 테스트]")
    @Test
    void test_2(){
//        var cartProduct = cartProductRepository.findById(1L).orElseThrow();
//        System.out.println("1번"cartProduct.getCart());
        var user = userRepository.findById(1L).orElseThrow();

        System.out.println(user.getNickname() + "의 장바구니: " + user.getCart());
        for (int i = 0; i < user.getCart().getCartProducts().size(); i++) {
            System.out.println("장바구니 물품 번호: " + user.getCart().getCartProducts().get(i).getId());
            System.out.println("물품 번호: " + user.getCart().getCartProducts().get(i).getProduct().getId());
        }

    }

}