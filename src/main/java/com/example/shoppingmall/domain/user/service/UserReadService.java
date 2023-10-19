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
//
//    public String login(LoginRequest loginRequest){
//        // 인증 과정
//        // 1. 없는 유저
//        // 2. 이메일과 비밀번호 매치
//        // 3. 로그인 성공 후 jwt 발급
//        var user = userRepository.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 이메일입니다."));
//
//        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
//            throw new BadCredentialsException("잘못된 비밀번호입니다.");
//        }
//
//        return JwtService.createJwt(loginRequest.getEmail(), secretKey, EXPIRED_AT);
//    }

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
