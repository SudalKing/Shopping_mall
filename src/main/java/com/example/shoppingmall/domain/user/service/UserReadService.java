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

    public List<UserDto> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public User getUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public UserDto toDto(User user){
        return new UserDto(user.getId(), user.getEmail(), user.getPassword(), user.getCreatedAt());
    }

}
