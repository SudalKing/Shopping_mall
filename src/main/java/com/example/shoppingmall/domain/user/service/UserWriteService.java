package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.application.usecase.user.CreateUserInfoSetUseCase;
import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import com.example.shoppingmall.domain.user.dto.req.RegisterUserCommand;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.example.shoppingmall.domain.user.util.Role;
import com.example.shoppingmall.domain.user.util.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

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
    public UserDto createUser(RegisterUserCommand registerUserCommand) throws Exception {
        if (!registerUserCommand.getPassword()
                .equals(registerUserCommand.getConfirmPassword())) {
            throw new Exception("비밀번호와 확인 비밀번호가 다릅니다!!");
        }

        var user = User.builder()
                .name(registerUserCommand.getName())
                .phoneNumber(registerUserCommand.getPhoneNumber())
                .email(registerUserCommand.getEmail())
                .password(passwordEncoder.encode(registerUserCommand.getPassword()))
                .role(Role.USER)
                .socialType(SocialType.WEB)
                .createdAt(LocalDateTime.now())
                .enabled(true)
                .infoSet(true)
                .build();

        AddressInfo addressInfo = registerUserCommand.getAddressInfo();
        BirthDate birthDate = registerUserCommand.getBirthDate();

        var savedUser = userRepository.save(user);
        log.info("User 저장 성공");

//        cartWriteService.createCart(savedUser);
//        log.info("장바구니 생성");
//
//        birthWriteService.createBirth(birthDate, savedUser);
//        addressWriteService.createAddress(addressInfo, savedUser);
//        log.info("생년월일, 주소 생성 완료");
        createUserInfoSetUseCase.createUserInfoSet(savedUser, addressInfo, birthDate);

        return toDto(userRepository.save(savedUser));
    }

    @Transactional
    public void deleteUser(User user){
        Long userId = user.getId();
        userRepository.deleteById(userId);
        birthWriteService.deleteBirth(user);
        addressWriteService.deleteAddress(user);
        cartWriteService.deleteCart(user);
    }

    @Transactional
    public void updateUser(User user, Map<String, Object> updates) {
        if (updates.containsKey("password")) {
            String password = passwordEncoder.encode(updates.get("password").toString());
            user.updatePassword(password);
        }

        if (updates.containsKey("phoneNumber")) {
            user.updatePhoneNumber(updates.get("phoneNumber").toString());
        }
        addressWriteService.updateAddress(user, updates);
        birthWriteService.updateBirth(user, updates);
        user.updateInfoSet();
        log.info("사용자 정보 업데이트 완료");
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
