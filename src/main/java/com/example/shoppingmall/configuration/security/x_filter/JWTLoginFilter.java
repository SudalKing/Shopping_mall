package com.example.shoppingmall.configuration.security.x_filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.shoppingmall.configuration.security.jwt.LoginRequest;
import com.example.shoppingmall.configuration.security.x_jwt.JWTUtil;
import com.example.shoppingmall.configuration.security.x_jwt.JWTVerifyResult;
import com.example.shoppingmall.domain.user.auth.CustomUserDetails;
import com.example.shoppingmall.domain.user.service.CustomUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CustomUserDetailService customUserDetailService;

    public JWTLoginFilter(AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService) {
        super(authenticationManager);
        this.customUserDetailService = customUserDetailService;
        setFilterProcessesUrl("/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws AuthenticationException {
        LoginRequest loginRequest = objectMapper.readValue(httpServletRequest.getInputStream(), LoginRequest.class);
        if (loginRequest.getRefreshToken() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword(), null // 오류나면 email or username 보기
            );
            return getAuthenticationManager().authenticate(token);
        } else {
            JWTVerifyResult jwtVerifyResult = JWTUtil.jwtVerifyResult(loginRequest.getRefreshToken());
            if (jwtVerifyResult.isSuccess()) {
                CustomUserDetails user = (CustomUserDetails) customUserDetailService.loadUserByUsername(jwtVerifyResult.getEmail());
                return new UsernamePasswordAuthenticationToken(
                        user, user.getAuthorities()
                );
            } else {
                throw new TokenExpiredException("Refresh Token Expired", Instant.now());
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException
    {
        CustomUserDetails user = (CustomUserDetails) authResult.getPrincipal();
        response.setHeader("auth_token", JWTUtil.makeAuthToken(user));
        response.setHeader("refesh_token", JWTUtil.makeRefreshToken(user));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
    }
}
