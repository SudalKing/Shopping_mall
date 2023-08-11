package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.RegisterUserCommand;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserWriteService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(RegisterUserCommand registerUserCommand){
        var user = User.builder()
                .nickname(registerUserCommand.getNickName())
                .email(registerUserCommand.getEmail())
                .password(registerUserCommand.getPassword())
                .build();
        return userRepository.save(user);
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
}
