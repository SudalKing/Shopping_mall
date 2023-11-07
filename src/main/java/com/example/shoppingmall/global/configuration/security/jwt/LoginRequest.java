package com.example.shoppingmall.global.configuration.security.jwt;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {

    private String email;
    private String password;
    private String refreshToken;
}
