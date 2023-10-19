package com.example.shoppingmall.configuration.security.oauth2.handler;

import com.example.shoppingmall.configuration.security.jwt.service.JwtService;
import com.example.shoppingmall.configuration.security.oauth2.CustomOAuth2User;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.example.shoppingmall.domain.user.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        if (customOAuth2User.getRole() == Role.GUEST) {
            log.info("게스트 유저");
            String accessToken = jwtService.createAccessToken(customOAuth2User.getEmail());
            log.info("소셜 로그인 Email: {}", customOAuth2User.getEmail());
            log.info("소셜 로그인 Token: {}", accessToken);

            response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);

            log.info("/ 리디렉션 실행");
            response.sendRedirect("/"); // 프론트 추가 정보 입력 폼으로 리다이렉트

            log.info("게스트 권한 -> 유저 권한. Refresh 토큰 생성");
            jwtService.sendAccessTokenAndRefreshToken(response, accessToken, null);
            User findUser = userRepository.findByEmail(customOAuth2User.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다!"));
            findUser.updateUserRole();
        } else {
            loginSuccess(response, customOAuth2User);
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User customOAuth2User) {
        String accessToken = jwtService.createAccessToken(customOAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(customOAuth2User.getEmail(), refreshToken);
    }
}
