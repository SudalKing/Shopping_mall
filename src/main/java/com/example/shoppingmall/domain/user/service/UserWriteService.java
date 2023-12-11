package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.dto.req.RegisterUserRequest;
import com.example.shoppingmall.domain.user.dto.req.UpdateUserInfoRequest;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.exception.EmailDuplicateException;
import com.example.shoppingmall.domain.user.exception.PasswordMismatchException;
import com.example.shoppingmall.domain.user.exception.SocialEmailAlreadyExistException;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.example.shoppingmall.domain.user.util.Role;
import com.example.shoppingmall.domain.user.util.SocialType;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserWriteService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final BirthWriteService birthWriteService;
    private final AddressWriteService addressWriteService;
    private final CartWriteService cartWriteService;

    @Transactional
    public UserDto createUser(final RegisterUserRequest registerUserRequest) {
        validateUser(registerUserRequest);

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

        cartWriteService.createCart(user);
        addressWriteService.createAddress(addressInfo, user);
        birthWriteService.createBirth(birthDate, user);

        return toDto(userRepository.save(savedUser));
    }

    public void deleteUser(final User user){
        userRepository.deleteById(user.getId());
    }

    public void updateUser(final User user, final UpdateUserInfoRequest updates) {
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

    public void logout(HttpServletRequest request, HttpServletResponse response, User user) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie: cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    public UserDto toDto(final User user){
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

    private void validateUser(final RegisterUserRequest registerUserRequest) {
        Optional<User> user = userRepository.findByEmailAndSocialType(registerUserRequest.getEmail(), SocialType.GOOGLE);
        // 소셜 이메일을 통해 회원가입을 진행할 때
        if (user.isPresent()) {
            throw new SocialEmailAlreadyExistException(registerUserRequest.getEmail(), ErrorCode.SOCIAL_EMAIL_EXIST);
        }

        // 이메일 중복 검증
        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new EmailDuplicateException(registerUserRequest.getEmail(), ErrorCode.EMAIL_DUPLICATION);
        }

        // 비밀번호 미스매치 검증
        if (!registerUserRequest.getPassword()
                .equals(registerUserRequest.getConfirmPassword())) {
            throw new PasswordMismatchException(registerUserRequest.getPassword(), ErrorCode.PASSWORD_MISMATCH);
        }

    }

}
