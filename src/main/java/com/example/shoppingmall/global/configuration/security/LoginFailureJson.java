package com.example.shoppingmall.global.configuration.security;

import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
public class LoginFailureJson {

    public void sendJsonResponse(HttpServletResponse response, ErrorCode errorCode, HttpStatus httpStatus) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", errorCode.getMessage());
        responseBody.put("status", errorCode.getStatus());
        responseBody.put("code", errorCode.getCode());

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

}
