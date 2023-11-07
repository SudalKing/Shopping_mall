package com.example.shoppingmall.global.configuration.security.jwt.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.shoppingmall.global.configuration.security.jwt.service.JwtService;
import com.example.shoppingmall.global.configuration.security.jwt.util.PasswordUtil;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final List<String> NO_CHECK_PATHS = Arrays.asList("/login", "/", "/logout", "/login/oauth2/code/**", "/user/signup",
        "/swagger-ui/**", "/v3/**", "/product/get/**", "/brand/get/**");

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String requestURI = request.getRequestURI();

        for (String path : NO_CHECK_PATHS) {
            if (new AntPathMatcher().match(path, requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String refreshToken = jwtService.getRefreshToken(request)
                .filter(jwtService::verifyToken)
                .orElse(null);

        if (refreshToken != null){
            validateAndRenewAccessToken(response, refreshToken);
        } else {
            authenticationAccessToken(request, response, filterChain);
        }

    }

    private void validateAndRenewAccessToken(HttpServletResponse response, String refreshToken) {

        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String renewRefreshToken = renewRefreshToken(user);
                    log.info("renewRefreshToken: {}", renewRefreshToken);
                    try {
                        jwtService.sendAccessTokenAndRefreshToken(response,
                                jwtService.createAccessToken(user.getEmail()),
                                renewRefreshToken);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    log.info("AccessToken 재발급");
                });

    }

    private void authenticationAccessToken(HttpServletRequest request,
                                           HttpServletResponse response,
                                           FilterChain filterChain) throws ServletException, IOException {
        log.info("authenticationAccessToken 호출");
        Optional<String> accessToken = jwtService.getAccessToken(request);

        if (accessToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        try {
              accessToken
                      .filter(jwtService::verifyToken)
                    .flatMap(jwtService::getEmail)
                    .flatMap(userRepository::findByEmail).ifPresent(this::saveAuthentication);
        } catch (TokenExpiredException e) {
            log.error("Token 만료 에러: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String renewRefreshToken(User user) {
        String renewRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(renewRefreshToken);
        userRepository.saveAndFlush(user);

        return renewRefreshToken;
    }

    private void saveAuthentication(User user) {
        String password = user.getPassword();
        if (password == null) {
            password = PasswordUtil.generateRandomPassword();
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(password)
                .roles(user.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, authoritiesMapper.mapAuthorities(userDetails.getAuthorities())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
