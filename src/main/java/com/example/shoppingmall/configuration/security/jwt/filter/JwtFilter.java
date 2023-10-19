package com.example.shoppingmall.configuration.security.jwt.filter;

import com.example.shoppingmall.configuration.security.jwt.service.JwtService;
import com.example.shoppingmall.configuration.security.jwt.util.PasswordUtil;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_PATH = "/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        if (request.getRequestURI().equals(NO_CHECK_PATH)){
            filterChain.doFilter(request, response);
            return;
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
                    jwtService.sendAccessTokenAndRefreshToken(response,
                            jwtService.createAccessToken(user.getEmail()),
                            renewRefreshToken);
                });
    }

    private void authenticationAccessToken(HttpServletRequest request,
                                           HttpServletResponse response,
                                           FilterChain filterChain) throws ServletException, IOException {
        log.info("authenticationAccessToken 호출");
        jwtService.getAccessToken(request)
                .filter(jwtService::verifyToken)
                .ifPresent(accessToken -> jwtService.getEmail(accessToken)
                        .ifPresent(email -> userRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));
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
