package com.example.shoppingmall.application.usecase.user;

import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.AddressWriteService;
import com.example.shoppingmall.domain.user.service.BirthWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateUserInfoSetUseCase {
    private final CartWriteService cartWriteService;
    private final AddressWriteService addressWriteService;
    private final BirthWriteService birthWriteService;

    @Transactional
    public void createUserInfoSet(User user, AddressInfo addressInfo, BirthDate birthDate) {
        cartWriteService.createCart(user);

        addressWriteService.createAddress(addressInfo, user);
        birthWriteService.createBirth(birthDate, user);
    }
}
