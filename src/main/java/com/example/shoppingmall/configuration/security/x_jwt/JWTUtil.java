package com.example.shoppingmall.configuration.security.x_jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.shoppingmall.domain.user.auth.CustomUserDetails;

import java.time.Instant;

public class JWTUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("jeongmin");
    private static final long AUTH_TIME = 2 * 60;
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7;

    public static String makeAuthToken(CustomUserDetails customUserDetails) {
        return JWT.create()
                .withSubject(customUserDetails.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond() + AUTH_TIME)
                .sign(ALGORITHM);
    }

    public static String makeRefreshToken(CustomUserDetails customUserDetails) {
        return JWT.create()
                .withSubject(customUserDetails.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static JWTVerifyResult jwtVerifyResult(String token){
        try{
            DecodedJWT decodedJWT = JWT.require(ALGORITHM).build().verify(token);
            return JWTVerifyResult.builder()
                    .success(true)
                    .email(decodedJWT.getSubject())
                    .build();
        } catch (Exception e){
            DecodedJWT decodedJWT = JWT.decode(token);
            return JWTVerifyResult.builder()
                    .success(false)
                    .email(decodedJWT.getSubject())
                    .build();
        }
    }
}
