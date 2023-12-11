package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.dto.res.UserInfoResponse;
import com.example.shoppingmall.domain.user.entity.UserAddress;
import com.example.shoppingmall.domain.user.entity.UserBirth;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.AddressRepository;
import com.example.shoppingmall.domain.user.repository.BirthRepository;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserReadService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BirthRepository birthRepository;


    public UserDto getUser(final Long id){
        var user = userRepository.findUserById(id);
        return toDto(user);
    }

    public User getUserByEmail(final String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> getUserPrincipal(final String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserEntity(final Long userId){
        return userRepository.findUserById(userId);
    }


    public UserInfoResponse findUserInfo(final User user) {
        UserAddress userAddress = addressRepository.findByUserId(user.getId());
        AddressInfo addressInfo = AddressInfo.builder()
                .postcode(userAddress.getPostcode())
                .address(userAddress.getAddress())
                .addressDetail(userAddress.getAddressDetail())
                .build();

        UserBirth userBirth = birthRepository.findByUserId(user.getId());
        BirthDate birthDate = BirthDate.builder()
                .year(userBirth.getYear())
                .month(userBirth.getMonth())
                .day(userBirth.getDay())
                .build();

        return new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getSocialType().toString(),
                user.isInfoSet(),
                addressInfo,
                birthDate
        );
    }

    public UserDto toDto(final User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.isEnabled());
    }
}
