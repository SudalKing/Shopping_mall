package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserReadService {

    private final UserRepository userRepository;

    public UserDto getUser(Long id){
        var user = userRepository.findUserById(id);
        return toDto(user);
    }

    public User getUserEntity(Long userId){
        return userRepository.findUserById(userId);
    }

    public List<UserDto> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto toDto(User user){
        return new UserDto(
                user.getId(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.isEnabled());
    }

    public User toEntity(UserDto userDto){
        return User.builder()
                .id(userDto.getId())
                .nickname(userDto.getNickname())
                .phoneNumber(userDto.getPhoneNumber())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .createdAt(userDto.getCreatedAt())
                .enabled(userDto.isEnabled())
                .build();
    }

}
