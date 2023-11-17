package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.application.usecase.user.CreateUserInfoSetUseCase;
import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.dto.req.RegisterUserRequest;
import com.example.shoppingmall.domain.user.dto.req.UpdateUserInfoRequest;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.exception.EmailDuplicateException;
import com.example.shoppingmall.domain.user.exception.PasswordMismatchException;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.example.shoppingmall.domain.user.util.Role;
import com.example.shoppingmall.domain.user.util.SocialType;
import com.example.shoppingmall.global.error.exception.ErrorCode;
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
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final BirthWriteService birthWriteService;
    private final AddressWriteService addressWriteService;
    private final CartWriteService cartWriteService;
    private final CreateUserInfoSetUseCase createUserInfoSetUseCase;


    @Transactional
    public UserDto createUser(RegisterUserRequest registerUserRequest) {
        // 이메일 중복 검증
        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new EmailDuplicateException(registerUserRequest.getEmail(), ErrorCode.EMAIL_DUPLICATION);
        }

        // 비밀번호 미스매치 검증
        if (!registerUserRequest.getPassword()
                .equals(registerUserRequest.getConfirmPassword())) {
            throw new PasswordMismatchException(registerUserRequest.getPassword(), ErrorCode.PASSWORD_MISMATCH);
        }

        var user = User.builder()
                .name(registerUserRequest.getName())
                .phoneNumber(registerUserRequest.getPhoneNumber())
                .email(registerUserRequest.getEmail())
                .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                .role(Role.USER)
                .socialType(SocialType.WEB)
                .createdAt(LocalDateTime.now())
                .enabled(true)
                .infoSet(true)
                .build();

        BirthDate birthDate = registerUserRequest.getBirthDate();
        AddressInfo addressInfo = registerUserRequest.getAddressInfo();

        var savedUser = userRepository.save(user);
        log.info("User 저장 성공");

        createUserInfoSetUseCase.createUserInfoSet(savedUser, addressInfo, birthDate);

        return toDto(userRepository.save(savedUser));
    }

    public void deleteUser(User user){
        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void updateUser(User user, UpdateUserInfoRequest updates) {
        if (updates != null) {

            if (updates.getName() != null) {
                user.updateName(updates.getName());
            }

            if (updates.getPhoneNumber() != null) {
                user.updatePhoneNumber(updates.getPhoneNumber());
            }

            if (updates.getPassword() != null) {
                user.updatePassword(passwordEncoder.encode(updates.getPassword()));
            }

            if (updates.getAddressInfo() != null) {
                addressWriteService.updateAddress(user, updates.getAddressInfo());
            }

            if (updates.getBirthDate() != null) {
                birthWriteService.updateBirth(user, updates.getBirthDate());
            }

            user.updateInfoSet();
            log.info("사용자 정보 업데이트 완료");
        }
    }

    public UserDto toDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.isEnabled()
        );

    }

}
