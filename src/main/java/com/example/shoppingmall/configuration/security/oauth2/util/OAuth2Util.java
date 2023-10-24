package com.example.shoppingmall.configuration.security.oauth2.util;

import com.example.shoppingmall.configuration.security.jwt.util.PasswordUtil;
import com.example.shoppingmall.configuration.security.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.example.shoppingmall.configuration.security.oauth2.userInfo.NaverOAuth2UserInfo;
import com.example.shoppingmall.configuration.security.oauth2.userInfo.OAuth2UserInfo;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.util.Role;
import com.example.shoppingmall.domain.user.util.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class OAuth2Util {

    private String nameKey;
    private OAuth2UserInfo oAuth2UserInfo;

    public OAuth2Util(String nameKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameKey = nameKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuth2Util of(SocialType socialType,
                                String usernameAttribute,
                                Map<String, Object> attributes) {
        if (socialType == SocialType.NAVER) {
            return ofNaver(usernameAttribute, attributes);
        } else {
            return ofGoogle(usernameAttribute, attributes);
        }
    }

    private static OAuth2Util ofNaver(String usernameAttribute, Map<String, Object> attributes) {
        return OAuth2Util.builder()
                .nameKey(usernameAttribute)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuth2Util ofGoogle(String usernameAttribute, Map<String, Object> attributes) {
        return OAuth2Util.builder()
                .nameKey(usernameAttribute)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
//                .password(PasswordUtil.generateRandomPassword())
                .password("0*!llpqasdp")
                .name(oAuth2UserInfo.getNickname())
                .phoneNumber("-")
                .enabled(true)
                .infoSet(false)
                .role(Role.GUEST)
                .build();
    }
}
