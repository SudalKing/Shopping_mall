package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.user.dto.RegisterUserCommand;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.example.shoppingmall.domain.user.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserWriteService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartWriteService cartWriteService;

//    @Transactional
    public UserDto createUser(RegisterUserCommand registerUserCommand){
        var user = User.builder()
                .nickname(registerUserCommand.getNickname())
                .phoneNumber(registerUserCommand.getPhoneNumber())
                .email(registerUserCommand.getEmail())
                .password(passwordEncoder.encode(registerUserCommand.getPassword()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .enabled(true)
                .build();

        var savedUser = userRepository.save(user);
        log.info("User 저장 성공");

        cartWriteService.createCart(savedUser);
        log.info("장바구니 생성");

        return toDto(userRepository.save(savedUser));
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @Transactional
    public void changeNickName(Long id, String nickName){
        var user = userRepository.findUserById(id);
        user.changeNickname(nickName);
        userRepository.save(user);
    }

    public UserDto toDto(User user){
        return new UserDto(
                user.getId(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.isEnabled()
        );

    }


}
