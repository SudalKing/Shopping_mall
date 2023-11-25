package com.example.shoppingmall.global.configuration.security.oauth2.service;

import com.example.shoppingmall.application.usecase.user.CreateUserInfoSetUseCase;
import com.example.shoppingmall.global.configuration.security.oauth2.CustomOAuth2User;
import com.example.shoppingmall.global.configuration.security.oauth2.util.OAuth2Util;
import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.example.shoppingmall.domain.user.util.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private final CreateUserInfoSetUseCase createUserInfoSetUseCase;

    private static final String NAVER = "naver";

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() - OAuth2 로그인 요청");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Util getAttribute = OAuth2Util.of(socialType, userNameAttributeName, attributes);
        User user = getUser(getAttribute, socialType);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes,
                getAttribute.getNameKey(),
                user.getEmail(),
                user.getRole()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if (NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        } else {
            return SocialType.GOOGLE;
        }
    }

    @Transactional
    public User getUser(OAuth2Util attributes, SocialType socialType) {
        User user = userRepository.findBySocialTypeAndSocialId(
                socialType, attributes.getOAuth2UserInfo().getId()
        );

        if (user == null) {
            return saveUser(attributes, socialType);
        }

        return user;
    }

    @Transactional
    public User saveUser(OAuth2Util attributes, SocialType socialType) {
        User user = attributes.toEntity(socialType, attributes.getOAuth2UserInfo());
        var savedUser = userRepository.save(user);

        AddressInfo addressInfo = AddressInfo.builder()
                .postcode("-")
                .address("-")
                .addressDetail("-")
                .build();

        BirthDate birthDate = BirthDate.builder()
                .year("-")
                .month("-")
                .day("-")
                .build();

        createUserInfoSetUseCase.createUserInfoSet(savedUser, addressInfo, birthDate);

        return savedUser;
    }
}
