package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.auth.CustomAuthority;
import com.example.shoppingmall.domain.user.dto.RegisterUserCommand;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.AuthRepository;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;

@RequiredArgsConstructor
@Service
public class UserWriteService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;

//    @Transactional
    public User createUser(RegisterUserCommand registerUserCommand){
        var user = User.builder()
                .nickname(registerUserCommand.getNickname())
                .phoneNumber(registerUserCommand.getPhoneNumber())
                .email(registerUserCommand.getEmail())
                .password(passwordEncoder.encode(registerUserCommand.getPassword()))
                .createdAt(LocalDateTime.now())
                .enabled(true)
                .build();
        var savedUser = userRepository.save(user);

        var auth = new HashSet<CustomAuthority>();
        auth.add(authRepository.save(new CustomAuthority("ROLE_USER", savedUser)));
        savedUser.setAuthorities(auth);

        return userRepository.save(savedUser);
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
