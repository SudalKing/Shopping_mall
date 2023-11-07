package com.example.shoppingmall.global.configuration.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
