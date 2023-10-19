package com.example.shoppingmall.configuration.security.oauth2.userInfo;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); // Google: sub, Naver: id, Kakao: id
    public abstract String getNickname();
}
