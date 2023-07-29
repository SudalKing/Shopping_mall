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
                .email(registerUserCommand.getEmail())
                .password(registerUserCommand.getPassword())
                .build();
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @Transactional
    public void changeEmail(Long id, String email){
        var user = userRepository.findUserById(id);
        user.changeEmail(email);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long id, String password){
        var user = userRepository.findUserById(id);
        user.changePassword(password);
        userRepository.save(user);
    }
}
