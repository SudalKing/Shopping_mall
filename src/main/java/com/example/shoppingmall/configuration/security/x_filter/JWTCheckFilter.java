package com.example.shoppingmall.configuration.security.x_filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.shoppingmall.configuration.security.x_jwt.JWTUtil;
import com.example.shoppingmall.configuration.security.x_jwt.JWTVerifyResult;
import com.example.shoppingmall.domain.user.auth.CustomUserDetails;
import com.example.shoppingmall.domain.user.service.CustomUserDetailService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

public class JWTCheckFilter extends BasicAuthenticationFilter {

    private final CustomUserDetailService customUserDetailService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService) {
        super(authenticationManager);
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearer == null || !bearer.startsWith("Bearer ")){
            chain.doFilter(request, response);
        }

        assert bearer != null;
        String token = bearer.substring("Bearer ".length());
        JWTVerifyResult jwtVerifyResult = JWTUtil.jwtVerifyResult(token);

        if (jwtVerifyResult.isSuccess()) {
            CustomUserDetails user = (CustomUserDetails) customUserDetailService.loadUserByUsername(jwtVerifyResult.getEmail());
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(userToken);
            chain.doFilter(request, response);
        } else {
            throw new TokenExpiredException("Token is not valid!!", Instant.now());
        }
    }
}
