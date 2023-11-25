package com.example.shoppingmall.global.configuration.security.oauth2.handler;

import com.example.shoppingmall.global.configuration.security.LoginFailureJson;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    private final LoginFailureJson loginFailureJson;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException
    {
        response.setCharacterEncoding("UTF-8");
        loginFailureJson.sendJsonResponse(response, ErrorCode.INVALID_INPUT_VALUE, HttpStatus.BAD_REQUEST);

        String redirectUrl = "https://orday.vercel.app/oauth2/callback";

        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(redirectUrl + "?error=" + URLEncoder
                .encode(ErrorCode.INVALID_INPUT_VALUE.getCode(), StandardCharsets.UTF_8));

        log.error("소셜 로그인에 실패했습니다. {}", exception.getMessage());
    }
}
