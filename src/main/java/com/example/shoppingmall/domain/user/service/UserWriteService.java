package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.RegisterUserCommand;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.example.shoppingmall.domain.user.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserWriteService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
