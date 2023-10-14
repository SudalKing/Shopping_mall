package com.example.shoppingmall.configuration.security.x_jwt;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JWTVerifyResult {
    private boolean success;
    private String email;
}
