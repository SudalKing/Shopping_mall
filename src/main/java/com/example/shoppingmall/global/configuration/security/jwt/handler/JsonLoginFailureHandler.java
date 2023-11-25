package com.example.shoppingmall.global.configuration.security.jwt.handler;

import com.example.shoppingmall.global.configuration.security.LoginFailureJson;
import com.example.shoppingmall.global.configuration.security.jwt.exception.EmailNotFoundException;
import com.example.shoppingmall.global.configuration.security.jwt.exception.EmailTypeSocialException;
import com.example.shoppingmall.global.configuration.security.jwt.exception.PasswordIncorrectException;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JsonLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final LoginFailureJson loginFailureJson;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException
    {
        response.setCharacterEncoding("UTF-8");

        if (exception.getCause() instanceof EmailTypeSocialException) {
            loginFailureJson.sendJsonResponse(response, ErrorCode.EMAIL_TYPE_SOCIAL, HttpStatus.BAD_REQUEST);

            log.error("소셜로그인으로 가입된 이메일입니다. {}", exception.getMessage());
        } else if (exception.getCause() instanceof EmailNotFoundException) {
            loginFailureJson.sendJsonResponse(response, ErrorCode.ENTITY_NOT_FOUND, HttpStatus.BAD_REQUEST);

            log.error("존재하지 않는 이메일 입니다. {}", exception.getMessage());
        } else {
            loginFailureJson.sendJsonResponse(response, ErrorCode.PASSWORD_INCORRECT, HttpStatus.BAD_REQUEST);

            log.error("비밀번호가 틀립니다. {}", exception.getMessage());
        }

    }
}
