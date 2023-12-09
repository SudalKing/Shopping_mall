package com.example.shoppingmall.global.configuration.security.jwt.filter;

import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.example.shoppingmall.global.configuration.security.jwt.service.JwtService;
import com.example.shoppingmall.global.configuration.security.jwt.util.PasswordUtil;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private static final List<String> NO_CHECK_PATHS = Arrays.asList(
            "/login", "/", "/logout",
            "/login/oauth2/code/**",
            "/oauth2/authorization/google",
            "/user/signup", "/swagger-ui/**", "/v3/**"
    );

    private static final List<String> NO_CHECK_SERVICE_PATHS = Arrays.asList(
            "/product/get/**",
            "/brand/get/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String requestURI = request.getRequestURI();
        Optional<String> accessToken = jwtService.getAccessToken(request);

        for (String path : NO_CHECK_PATHS) {
            if (new AntPathMatcher().match(path, requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        for (String servicePath : NO_CHECK_SERVICE_PATHS) {
            if (new AntPathMatcher().match(servicePath, requestURI) && accessToken.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        authenticationAccessToken(request, response, filterChain);
    }



    private void authenticationAccessToken(HttpServletRequest request,
                                           HttpServletResponse response,
                                           FilterChain filterChain) throws ServletException, IOException {
//        log.info("authenticationAccessToken 호출");
        Optional<String> accessToken = jwtService.getAccessToken(request);

        if (accessToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("Access token is Null");
            return;
        }

        if (!jwtService.verifyToken(accessToken.get())) {
//            log.error("Token 검증 실패");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Optional<Cookie> cookie = Optional.ofNullable(jwtService.findCookie(request));

            if (cookie.isEmpty()) {
                log.error("Cookie is Null");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String refreshToken = cookie.get().getValue();

            validateAndRenewAccessToken(request, response, refreshToken);
            return;
        }

        jwtService.getEmail(accessToken.get())
                        .flatMap(userRepository::findByEmail)
                                .ifPresent(this::saveAuthentication);

        filterChain.doFilter(request, response);
    }

    private void validateAndRenewAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) {

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
